create table etudiants (
    id int,
    nom varchar(50),
    prenom varchar(50),
    date_naissance date,
    email varchar(50),
    primary key (id)
);

create table enseignants (
    id int,
    nom varchar(50),
    prenom varchar(50),
    grade varchar(20),
	primary key (id)
);

CREATE TABLE cours (
  `id` INT NOT NULL,
  `intitule` VARCHAR(45) NULL,
  `enseignant_id` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `cours_enseignants_idx` (`enseignant_id` ASC) VISIBLE,
  CONSTRAINT `cours_enseignants`
    FOREIGN KEY (`enseignant_id`)
    REFERENCES `tp_ecole`.`enseignants` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

create table inscriptions
 (
    etudiant_id int,
    cours_id int
);

-- Insert data
insert into etudiants  values
(1,'Ali', 'Ben Ahmed', '2001-05-12', 'ali@mail.com'),
(2,'Sara', 'Trabelsi', '2002-07-20', 'sara@mail.com'),
(3,'Omar', 'Kacem', '2001-11-03', 'omar@mail.com');

insert into enseignants  values
(1,'Mabrouk', 'Hassen', 'Professeur'),
(2,'Jemni', 'Sonia', 'Maître de conférence');

insert into cours (intitule, enseignant_id) values
('Mathématiques', 1),
('Physique', 2);

insert into inscriptions  values
(1, 1),
(2, 1),
(2, 2),
(3, 2);

-- Select all students
select * from inscriptions;
select * from cours;

SELECT e.nom, e.prenom
FROM etudiants e
JOIN inscriptions i ON e.id = i.etudiant_id
JOIN cours c ON i.cours_id = c.id
WHERE c.intitule = "mathématiques";

SELECT c.intitule
FROM cours c
JOIN enseignants en ON c.enseignant_id = en.id
WHERE en.nom = 'Mabrouk';


SELECT e.nom, e.prenom, COUNT(i.cours_id) AS nbcours
FROM etudiants e
JOIN inscriptions i ON e.id = i.etudiant_id
GROUP BY e.id, e.nom, e.prenom
HAVING COUNT(i.cours_id) > 1;

select * from etudiants
