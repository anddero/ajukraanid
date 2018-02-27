==========
Ajukraanid
==========

TTÜ Tarkvaratehnika projeckt IDK0071
------------------------------------

Projekti "arendamise" salv.

Muudatusettepanekute tegemine
-----------------------------

Vahearuanne I
--------------
Nils-Emil: backend saab HTTP requestide peale tekitada mänge, nende mängudega saab liituda, kui liituda ei saa, tagastab backend API vastava teada mis on meie documentatsioonis kirjas.


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

	- Client: {“Action”:”CreateGame”}
	- Server: {“Action”:”CreateGame”,”Code”:”4623”}
Join Existing Game + Insert Name

	- Client: 	{“Action”:”JoinGame”, “Code”:“8712”,“Name”:“blabla”}
	- Server: 	Fetch State response OR Error
{“Action”:”FetchState”,
			“State”:“Error”,
			“Data”: “Gamecode not found”}

Fetch State (Background process)

	- Client: 	{“Action”:”FetchState”, “GameCode”:”1234”} // unustasime ära selle algul
	- Server: 	Lobby: {“Action”:”FetchState”,“State”:“Lobby”, “Data”: [“name1”,”name2”,...]}

Question: everyone is answering a question

    - {“Action”:”FetchState”,“State”:“Question”,“Data”: {“Number”:”2”,“Question”:“Que pikk question?”}}

Grading: everyone is grading answers to the previous question

    - {“Action”:”FetchState”,“State”:“Grading”,“Data”: {“Question”:”hgrwetgqwe?”,“Number”:”3”,“Answers”:[“ans1”,”ans2”,...]}}

Score: inimesed näevad kogu mängu tulemusi

    - {“Action”:”FetchState”,“State”:“Results”,“Data”: [“Name1”: “score”,“Name2”: “243”,...]}
    
Begin Game

    - Client: {“Action”:”BeginGame”,”Code”:”4623”}
    - Server: Fetch State response (including error)

Insert Answer - player answers a question with a custom answer

	- Client:	{“Action”:”SubmitAnswer”,“Name”: “Porgand”,“Question”: “6”,“Answer”: “Cuz im black”}
	- Server: default OK HTTP response (no message, no json, no no)
	
Give Score - player gives points for another players answers

	- {“Action”:”GiveScore”,“PlayerName”:“Nils”, // kes annab punkte “Target”:”Aivar” // kellele punkte antakse}

    
