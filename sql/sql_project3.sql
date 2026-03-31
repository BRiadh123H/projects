select distinct Nom 
from AGENCE ,COMPTE
where  AGENCE.Num_Agence =COMPTE.Num_Agence;

select CLIENT.Nom 
from CLIENT,AGENCE ,COMPTE
where  AGENCE.Num_Agence =COMPTE.Num_Agence
and CLIENT.Num_Client=COMPTE.Num_Client
and AGENCE.Ville='La Rochelle';

select CLIENT.Nom 
from CLIENT,AGENCE ,COMPTE
where  AGENCE.Num_Agence =COMPTE.Num_Agence
and CLIENT.Num_Client=COMPTE.Num_Client
and AGENCE.Ville='La Rochelle';
union 
select CLIENT.Nom 
from CLIENT,AGENCE ,EMPRUNT
where  AGENCE.Num_Agence =EMPRUNT.Num_Agence
and CLIENT.Num_Client=EMPRUNT.Num_Client
and AGENCE.Ville='La Rochelle';

select CLIENT.Nom 
from CLIENT,AGENCE ,COMPTE
where  AGENCE.Num_Agence =COMPTE.Num_Agence
and CLIENT.Num_Client=COMPTE.Num_Client
and AGENCE.Ville='La Rochelle';
intersect
select CLIENT.Nom 
from CLIENT,AGENCE ,EMPRUNT
where  AGENCE.Num_Agence =EMPRUNT.Num_Agence
and CLIENT.Num_Client=EMPRUNT.Num_Client
and AGENCE.Ville='La Rochelle';

select CLIENT.Nom 
from CLIENT,AGENCE ,COMPTE
where  AGENCE.Num_Agence =COMPTE.Num_Agence
and CLIENT.Num_Client=COMPTE.Num_Client
and AGENCE.Ville='La Rochelle';
except
select CLIENT.Nom 
from CLIENT,AGENCE ,EMPRUNT
where  AGENCE.Num_Agence =EMPRUNT.Num_Agence
and CLIENT.Num_Client=EMPRUNT.Num_Client
and AGENCE.Ville='La Rochelle';

select CLIENT.Nom ,CLIENT.Ville 
from CLIENT ,COMPTE
where CLIENT.Num_Client=COMPTE.Num_Client;
--
select CLIENT.Nom ,CLIENT.Ville 
from CLIENT 
where Num_Client in (
    select Num_Client from COMPTE) ;

select CLIENT.Nom ,CLIENT.Ville
from CLIENT,AGENCE ,COMPTE
where  AGENCE.Num_Agence =COMPTE.Num_Agence
and CLIENT.Num_Client=COMPTE.Num_Client
and AGENCE.Nom='Paris-Etoile';
--
select CLIENT.Nom ,CLIENT.Ville
from CLIENT
where Num_Client in (
    select Num_Client from COMPTE
    where Num_Agence in (
        select Num_Agence from AGENCE
        where AGENCE.Nom='Paris-Etoile'
    )
);

select CLIENT.Nom 
from CLIENT,AGENCE 
where CLIENT.Num_Client=COMPTE.Num_Client
and Num_Agence in (
    select Num_Agence 
    from CLIENT,AGENCE 
    where CLIENT.Num_Client=COMPTE.Num_Client
    and CLIENT.Nom='Claude');

select AGENCE.Nom
from AGENCE
where AGENCE.Actif > (
    select max (AGENCE.Actif) from AGENCE
    where AGENCE.Ville='Orsay' 
);

select CLIENT.Nom 
from CLIENT,COMPTE 
where CLIENT.Num_Client=COMPTE.Num_Client
and COMPTE.Num_Agence in(
    select Num_Agence
    from AGENCE
    where AGENCE.Ville='Orsay' 
)