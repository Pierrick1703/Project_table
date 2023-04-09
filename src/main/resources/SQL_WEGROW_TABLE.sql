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
    NomTable TEXT,
    ViewUserId TEXT,
    EditUserId TEXT,
    AdminUserId TEXT
);

insert into dataTable(idTable,NomTable,ViewUserId,EditUserId,AdminUserId) values
(1,"Personnel","","",""),
(2,"Entreprise","","","");

insert into Colonne(IdColonne,NomColonne,TypeColonne,TableId) values
(1,"Nom","String","1"),
(2,"Prenom","String","1"),
(3,"Age","int","1"),
(4,"Code Postal","String","1"),
(5,"Nom","String","2"),
(6,"Siret","int","2"),
(7,"Métier","String","2"),
(8,"Code Postal","String","2");


insert into Ligne(IdLigne,ValeurLigne,NumeroLigne,FormuleLigne,ColonneId)
values 
("Dupont",1,"",1),
("Jardin",2,"",1),
("Cot",3,"",1),
("Train",4,"",1),
("Patrick",1,"",2),
("Martin",2,"",2),
("Manon",3,"",2),
("Charlotte",4,"",2),
("23",1,"",3),
("21",2,"",3),
("20",3,"",3),
("22",4,"",3),
("95200",1,"",4),
("95300",2,"",4),
("95300",3,"",4),
("95110",4,"",4),