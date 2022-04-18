# Our Mail Delivery Application

The application will store mails and packages that post workers should delivered. <br />
It will be used by each postworker to see which mail he sould deliver and he will have access to all information about these mail. <br />
<br />
The postworker has the possibilities to add one mail or to change some information about the mail. Like he will be able to modify his account information.

## Information to log in

In order to make some test some demo data are already saved in the Firebase database:

#### Post Worker
- Abdullah Binjos, binjabdu@gmail.com, @dminHevs01,Avenue de la Gare 1950 Sion, 079. <br />
- Megane Solliard, smeg@gmail.com, @dminHevs01, Rue des carou 12 3960 Sierre, 079. <br />
- Centrale Centrale, centrale@poste.ch, @dminHevs01, Rue centrale 1950 Sion, 0000<br />



#### Mails
- PostWorker Assigned = Centrale, From Abdullah, To Meg, Type Letter, A-Mail, Rte de Roumaz, 1965 Saviese. 
- PostWorker Assigned = Centrale, From Meg, to Emilie, Type Packages, B-Mail, Avenue de la Gare 1950 Sion.
- PostWorker Assigned =Megane, From Loic, to Jane, Type Packages, B-Mail, Plaine Bellevue 3960 Sierre.

<br />

## Information about mail creation
In order to make a valitation on the shipping address, a JSON file containing all the cities in Switzerland will be loaded. We will then check if the city entered correspond to one of the cities stored in this file.
<br />

Unfortunately, we haven't found a file containing all the small villages. **Only a city can be entered !**
<br />
<br />

### Enjoy our application ;)
Made with love and fun by Abdullah Binjos & Mégane Solliard
