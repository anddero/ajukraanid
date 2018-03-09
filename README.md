Ajukraanid
==========


TTÜ Tarkvaratehnika projeckt IDK0071
------------------------------------

Projekti "arendamise" salv.

Muudatusettepanekute tegemine
-----------------------------


Arendamine
----------

Frontend - Aivar Loopalu (164416IAPB)

REST API, mänguloogika - Nils-Emil Lille (155082IAPB)

Andmebaas, turvalisus  - Karl-Andero Mere (164718IAPB)


Kirjeldus
---------
Mäng toimub mitmes seadmes korraga. On 2 vaadet: mängija ja host. Hosti ekraani pealt kuvatakse küsimus, millele kõik mängijad peavad vastama. Seejärel kui kõik on vastanud kuvatakse kõik vastused hosti ekraanil anonüümselt ning seejärel peavad kõik mängijad valima parima vastuse. Mida rohkem saab mängija hääli, seda rohkem saab ta punkte.
Aruanne


Iteratsioon I
-------------

Aivar: Töötav algeline kasutajaliides ja andmevahetus serveriga.

Kõik: Sõnumivahetuse JSON formaat.

Nils: Töötav veebiserver ning ühilduvus Java rakendusega.

Andero: Andmebaas ning ühilduvus Java rakendusega. Pilve?

Funktsionaalsus:
Saab luua uut mänguinstantsi, mille vastu saab koodi.
Saab sisestada koodi ja registreerida mängija nime (veebiliidese kaudu).

Iteratsioon II
--------------
Aivar: Töötav kasutajaliides.

Nils: Töötav mänguloogika ja server.

Andero: Turvalisus ja lõplik andmebaas.

Kõik: Küsimuste loomine confi faili.

Funktsionaalsus:
Ooteruum HOST-i vaates, kus on näha mänguga liitunuid.
Mängu alustamine.
Küsimustele vastamine.
Vastuste hindamine.
Tulemuste näitamine.
Mängu lõpetamine.

Iteratsioon III
---------------
Aivar: Ilus disain, animatsioonid.

Nils, Andero: Veebiliides küsimuste andmebaasi redigeerimiseks.

Kõik: Kui jõuab.
Statistilised vidinad andmebaasi andmete kohta. Turu uuring statistiliste vidinate nõudluse suhtes.
Mängijad saavad pärast mängu lõppu küsimusi hinnata.


JSON AGREEMENT
--------------

Insert URL (Static page)

Start New Game

	- Client: 
	    - {"Action":"CreateGame"}
	- Server: 
	    - {"Action":"NewGame","Code":7488}
Join Existing Game + Insert Name

	- Client: 	
	    - {"Action":"JoinGame", "Code":"8712","Name":"blabla"}
	- Server: 	     Fetch State response OR Error
        - {"Action":"FetchState","State":"Lobby","Data":"[{\"name\":\"Andero\"}]"}
        - {"Action":"FetchState","State":"Error","Data":"Such username is already taken."}
	    - {"Action":"FetchState","State":"Error","Data":"Did not find such game with game code: 8712"}


Fetch State (Background process)

	- Client: 	
	    - {“Action”:”FetchState”, “GameCode”:”1234”}
	- Server:
	    - {"Action":"FetchState","State":"Lobby","Data":"[{\"name\":\"Andero\"}, {\"name\":\"Nils\"}, {\"name\":\"Aivar\"}]"}
	    - {"Action":"FetchState","State":"Error","Data":"Did not find such game with game code: 1574"}


Grading: everyone is grading answers to the previous question
      
    - Client:
        - {"Action": "GivePoints", "Code": "1734", "Question number": "1", "Name": "Nils","Target": "Aivar"}
    - Server: 
        - {"Action":"FetchState","State":"Error","Data":"Question number does not match he current games' state"}
        - {"Action":"FetchState","State":"Success","Data":"Your points were given to Nils"} 
        - {"Action":"FetchState","State":"Error","Data":"Can not give points, beacause you allready gave points"}
        - {"Action":"FetchState","State":"Error","Data":"You can not give points to yourself"}

Score: inimesed näevad kogu mängu tulemusi
    
    - Client
        - {"Action":"GetPoints", "Code": "1642"}
    - Server
        - {"Action":"FetchState","State":"Points","Data":"{Andero=0, Nils=0, Aivar=100}"}
    
Begin Game

    - Client: 
        - {"Action":"BeginGame", "Code":"4623"}
    - Server: 
        - Fetch State response (including error)

Insert Answer - player answers a question with a custom answer

	- Client:	
	    - {"Action":"SubmitAnswer","Name": "Porgand","Question number": “6”,"Answer": "Cuz im black"}
	- Server: 
	    - default OK HTTP response (no message, no json, no no)
	
Question: everyone is answering a question
    - QUESTION LOGIC IS NOT IMPLEMENTED YET.
