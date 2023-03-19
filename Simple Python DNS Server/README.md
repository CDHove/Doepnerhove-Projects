# Carsten Doeponerhove,CSCI4211S23,03/01/2023

## python,DNSServerV3.py,,DNSServerV3.py

### Compilation and Execution
To compile and run the server, first insure that the nessicary python libraries are
installed. The libraries used for this server are as follows:

- threading
- os
- socket
- dnspython
- time

These can be installed via PIP or your preferred installation method.  

Running this server simply involves entering the command ***"python DNSServerV3.py"***. This will start the 
server listening on the designated port for connections.

### Discription

This server functions as a DNS server. It uses a text file DNS_mapping as a local cache and when recieving connections
it will first check the local cache for the desired hostname and if found will return it to the requester. If the hostname is not found it will instead use dns.resolver to query another dns server. After reciving the result from that server, it will store the result in the cache then return the query to the requestor. The server stores every request in a log file named dns-server-log.  
 
The server utilizes threading to answer each request. Each request to the server will create a new thread that will obtain the desired address, send the address back to the requestor, and terminate itself. Two threads are constantly running on the server; monitor and save. These monitor for an exit statement and save the cache information to DNS_mapping respectively  

The server also listens on port 5001 by default. If you wish to change the port simply modify the port variable on line 31.