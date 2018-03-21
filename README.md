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
