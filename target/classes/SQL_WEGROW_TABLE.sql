CREATE TABLE Ligne(
	IdLigne int auto_increment primary key,
    ValeurLigne TEXT,
    NumeroLigne int,
    FormuleLigne TEXT,
    ColonneId int
);

CREATE TABLE Colonne(
	IdColonne int auto_increment primary key,
    NomColonne TEXT,
    TypeColonne TEXT,
    TableId int
);

CREATE TABLE dataTable(
	IdTable int auto_increment primary key,
    nameTable TEXT,
    jsonTable JSON,
    ViewUserId TEXT,
    EditUserId TEXT,
    AdminUserId TEXT
);

CREATE TABLE privillege(
	IdPrivillege int auto_increment primary key,
    NomTable TEXT,
    adresseMailUser TEXT,
    Viewer boolean,
    Editer boolean,
    Administre boolean
);

CREATE TABLE Utilisateur(
	IdUtilisateur int auto_increment primary key,
    nom TEXT,
    prenom TEXT,
	adresseMail TEXT,
    telephone TEXT,
    motDePasse TEXT
);

CREATE TABLE groupe(
	IdGroupe int auto_increment primary key,
    NomGroupe TEXT
);

CREATE TABLE groupeTable(
	IdGroupeTable int auto_increment primary key,
    NomGroupe TEXT,
    NomTable TEXT,
    Viewer boolean,
    Editer boolean,
    Administre boolean
);

CREATE TABLE groupeMember(
	IdGroupeMember int auto_increment primary key,
    NomGroupe TEXT,
	adresseMailUser TEXT
);

CREATE VIEW `ViewGroupe`
AS select gm.adresseMailUser AS adresseMailUser,gm.NomGroupe AS NomGroupe,gt.NomTable AS NomTable,gt.Viewer AS Viewer,gt.Editer AS Editer
from ((groupe g join groupeMember gm) join groupeTable gt) where g.NomGroupe = gm.NomGroupe and g.NomGroupe = gt.NomGroupe order by gm.adresseMailUser,gm.NomGroupe

insert into groupe (NomGroupe) values
("Finance"),
("Visiteur")

insert into groupeTable (NomGroupe,NomTable,Viewer,Editer,Administre) values
("Finance","Compta",false,true,false),
("Finance","Table de travail",false,true,false),
("Finance",'Table',false,true,false),
("Finance",'Table2',false,true,false),
("Visiteur","htrh",true,false,false),
("Visiteur","jopjo",true,false,false),
("Visiteur","dazda",true,false,false)

insert into groupeMember (NomGroupe,adresseMailUser) values
("Finance","cottin.pierrick@gmail.com"),
("Finance","klattjohan@gmail.com"),
("Finance","aichabozize1@gmail.com"),
("Finance","clem0123@hotmail.fr"),
("Visiteur","cottin.pierrick@gmail.com"),
("Visiteur","klattjohan@gmail.com"),
("Visiteur","aichabozize1@gmail.com"),
("Visiteur","clem0123@hotmail.fr");

insert into Utilisateur (nom,prenom,adresseMail,telephone,motDePasse) values
("Cottin","Pierrick","cottin.pierrick@gmail.com","0601020304","Azerty1!"),
("Klatt","Johan","klattjohan@gmail.com","0601020304","A123456789!"),
("Aliou","Aicha","aichabozize1@gmail.com","0601020304","Qwerty1!"),
("Gobert","Clément","clem0123@hotmail.fr","0601020304","Certif1!"),
("Admin","Admin","Admin.Admin@gmail.com","0601020304","Table1!");

insert into dataTable (nameTable,jsonTable,ViewUserId,EditUserId,AdminUserId) values
('Compta','{"Colonne":[{"Ligne":[{"Numero":0,"Valeur":"0","Formule":""},{"Numero":1,"Valeur":"1","Formule":""},{"Numero":2,"Valeur":"2","Formule":""}],"Type":"String","Nom":"Numéro"},{"Ligne":[{"Numero":1,"Valeur":"Azerty","Formule":""},{"Numero":2,"Valeur":"Qwerty","Formule":""},{"Numero":3,"Valeur":"test","Formule":""}],"Type":"String","Nom":"Nom"},{"Ligne":[{"Numero":1,"Valeur":"PAzerty","Formule":""},{"Numero":2,"Valeur":"PQwerty","Formule":""},{"Numero":3,"Valeur":"Ptest","Formule":""}],"Type":"String","Nom":"Prenom"},{"Ligne":[{"Numero":1,"Valeur":"1","Formule":""},{"Numero":2,"Valeur":"2","Formule":""},{"Numero":3,"Valeur":"3","Formule":""}],"Type":"Int","Nom":"numéro"}],"Nom":"Compta"}',null,null,null),('Table de travail','{"Colonne":[{"Ligne":[{"Numero":0,"Valeur":"0","Formule":""},{"Numero":1,"Valeur":"1","Formule":""},{"Numero":2,"Valeur":"2","Formule":""}],"Type":"String","Nom":"Numéro"},{"Ligne":[{"Numero":1,"Valeur":"Entrprise A","Formule":""},{"Numero":2,"Valeur":"Entrprise B","Formule":""},{"Numero":3,"Valeur":"Entrprise C","Formule":""}],"Type":"String","Nom":"Entrprise"},{"Ligne":[{"Numero":1,"Valeur":"0001","Formule":""},{"Numero":2,"Valeur":"0002","Formule":""},{"Numero":3,"Valeur":"0003","Formule":""}],"Type":"Int","Nom":"Siret"}],"Nom":"Table de travail"}',null,null,null);
