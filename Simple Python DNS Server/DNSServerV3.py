# doepn008@umn.edu
# Spring 2023 CSci4211: Introduction to Computer Networks
# This program serves as the server of DNS query.
# Written in Python v3.

import sys, threading, os, random
from socket import *
import dns.resolver
import time

DNS_FILE = "DNS_mapping.txt"

DNSCache = {}
holdCache = {}


def main():
	# Check if DSN_FILE exists (creates it if it does not)
	try:
		file = open(DNS_FILE, "r")
		# reads data from file (if any)
		data = file.read().split("\n")
		for index in data:
			hold = index.split(",")
			if len(hold) == 2:
				DNSCache[hold[0]] = hold[1]
	except FileNotFoundError:
		file = open(DNS_FILE, "w+")
 
	host = "localhost" # Hostname. It can be changed to anything you desire.
	port = 5001 # Port number.

	#create a socket object, SOCK_STREAM for TCP
	serverSocket = socket(AF_INET, SOCK_STREAM)

	#bind socket to the current address on port
	serverSocket.bind(('', port))

	#Listen on the given socket maximum number of connections queued is 20
	serverSocket.listen(20)

	monitor = threading.Thread(target=monitorQuit, args=[])
	monitor.start()
	
	save = threading.Thread(target=saveFile, args=[])
	save.start()

	print("Server is listening...")

	while 1:
		#blocked until a remote machine connects to the local port
		connectionSock, addr = serverSocket.accept()
		server = threading.Thread(target=dnsQuery, args=[connectionSock, addr[0]])
		server.start()


def dnsQuery(connectionSock, srcAddress):
	data = connectionSock.recv(1024).decode()
	csvFile = open("dns-server-log.csv", "a")
	
	# Checks cache for item, otherwise queries dns server
	if data in DNSCache:
		response = data + ":" + DNSCache[data] + ":CACHE"
		log = "\n" + data + "," + DNSCache[data] + ",CACHE"
  
		print("Sending Cache Response: " + response)
		connectionSock.send(response.encode())
		csvFile.write(log)
	else:
		try: 
			result = dns.resolver.query(data, 'A')
		except:
			response = data + ":hostnotfound:CACHE"
			log = "\n" + data + ",hostnotfound,CACHE"
			connectionSock.send(response.encode())
			csvFile.write(log)
			csvFile.close()
			connectionSock.close()
			return
		item = result[0]
		DNSCache[data] = item.to_text()
		holdCache[data] = item.to_text()
   
		response = data + ":" + item.to_text() + ":API"
		log = "\n" + data + "," + DNSCache[data] + ",CACHE"

		print("Sending API Response: " + response)
		connectionSock.send(response.encode())
		csvFile.write(log)
	csvFile.close()
	connectionSock.close()
	
  
def dnsSelection(ipList):
	# Unused. DNS seleciton is done in dnsQuery() and simply takes the first response of the list
	return


def saveFile():
	while 1:
		if len(holdCache.values()) != 0:
			writeFile = open(DNS_FILE, "a")
			for key, value in holdCache.items():
				writeFile.write("\n" + key + "," + value)
			holdCache.clear()
			writeFile.close()
		time.sleep(15)


def monitorQuit():
	while 1:
		sentence = input()
		if sentence == "exit":
			os.kill(os.getpid(),9)

main()
