Concurent Banking System
Made with Java and a little help of Maven
I used Sockets to communicate between client/ admin and server. 

What classes do: 
- Admin- it contains aplication for admin usage with 4 options: 
              -> 1- Dodaj nowego klienta (add new client)
              -> 2- Edytuj imię danego klienta (edit name of client You choose)
              -> 3- Edytuj nazwisko danego klienta (edit surname of client You choose)
              -> 4- Edytuj PESEL danego klienta (edit PESEL - polish personal number)
- Client- it contains aplication for client usage with 4 options: 
              -> 1- Wypłać pieniądze (Withdraw the money)
              -> 2- Wpłać pieniądze (Donate money)
              -> 3- Wykonaj przelew (Make a transfer)
              -> 4- Sprawdź stan konta (Check account balance)
              -> 5- Exit
- EchoServer- listening for new connection and then create new EchoServerThread
- EchoServerThread- it recives information from client/ admin, then do work like add founds to account, and after that it send message to client/ admin back
- File Manager- saves and load data from txt document (database.txt)
- Administrator - model of admin
- Bank User - model of client 

Link to DEMO pdf: https://drive.google.com/file/d/10lykIoZ6l_nsewRnPT7nZ7ZQ4KCfa0oe/view?usp=sharing

What I Could do better or improve? 
1. Definetley use more clean code 
2. Try to use more for loops and make more simple methods to don't copy same code
3. Work more on try catch and with exceptions
4. 100% English syntax and coments
5. Write tests in for example JUnit 
6. After stoping clint aplication, server crashes

If You want to try aplication by Yourself: 
1. Clone project
2. Make 2 Clients aps, 1 admin app and 1 EchoServer app 
3. Start EchoServer
4. Start Client 
5. Client login: szymus password: 1q2w3e
6. Client2 login: zenus password: zenus1
7. Start Admin 
8. Admin login: admin password: root
9. Start EchoServer and have fun
