create database university;
use university;
create table departements(
id_dept int   ,
nom_dept varchar (20),
primary key ( id_dept)
);

create table etudiants(
id_etudiant int   ,
nom varchar (20),
prenom varchar (20),
age int ,
id_dept int ,
moyenne float,
email varchar(50),

FOREIGN KEY (`id_dept`)
    REFERENCES `university`.`departements` (`id_dept`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
primary key ( id_etudiant)
);

create table modules(
id_module int,
nom_module varchar(50),
id_dept int ,
FOREIGN KEY (`id_dept`)
    REFERENCES `university`.`departements` (`id_dept`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
primary key ( id_module)
);
insert into departements values 
(1, 'GL'),
(2, 'IIA'),
(3, 'RT');
insert into etudiants values
(1, 'Ali', 'Karim', 21, 1, 12.5, 'ali.karim@univ.tn'),
(2, 'Meriem', 'Saadi', 22, 2, 15.3, 'meriem.saadi@univ.tn'),
(3, 'Sami', 'Ghorbel', 20, 1, 9.8, 'sami.ghorbel@univ.tn'),
(4, 'Amina', 'Ben Salah', 23, 3, 13.4, 'amina.bensalah@univ.tn'),
(5, 'Youssef', 'Trabelsi', 21, 1, 16.0, 'youssef.trabelsi@univ.tn'),
(6, 'Nour', 'Zayani', 22, 2, 10.0, 'nour.zayani@univ.tn');
INSERT INTO modules (id_module, nom_module, id_dept)
VALUES
(1, 'Programmation', 1),
(2, 'Réseaux', 3),
(3, 'Base de données', 1),
(4, 'IA', 2),
(5, 'Systèmes', 3);
select * from departements;
select * from etudiants;
select * from modules;

select * from etudiants
join departements on departements.id_dept=etudiants.id_dept;

select * from etudiants
join departements on departements.id_dept=etudiants.id_dept
where moyenne>12;

select * from modules 
JOIN departements ON departements.id_dept = modules.id_dept
where nom_dept='GL';

select * from etudiants
order by  moyenne desc;

select * from etudiants
WHERE nom LIKE 'A%';


SELECT nom_dept , count(id_etudiant)
FROM etudiants
join departements
where departements.id_dept=etudiants.id_dept
group by nom_dept;

SELECT nom_dept , avg(id_etudiant)
FROM etudiants
join departements
where departements.id_dept=etudiants.id_dept
group by nom_dept;

select *
FROM etudiants
order by moyenne desc
limit 1;

SELECT nom_dept , avg(moyenne)
FROM etudiants
join departements
where departements.id_dept=etudiants.id_dept
group by nom_dept
order by avg(moyenne) desc
limit 1;

---------------------------------------------------------------------------------
SELECT *
FROM etudiants ,departements
where etudiants.id_dept=departements.id_dept;

SELECT nom_dept , avg(moyenne)
FROM etudiants ,departements
where etudiants.id_dept=departements.id_dept
group by nom_dept
order by avg(moyenne) desc
limit 1
;