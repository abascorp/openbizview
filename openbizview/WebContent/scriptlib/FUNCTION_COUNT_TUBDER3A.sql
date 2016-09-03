create or replace FUNCTION "COUNT_TUBDER03A" (pbusqueda varchar2)

--*---------------------------------------*--
--* FUNCION QUE RETORNA EL CONTEO DE LOS  *--
--* REGISTROS HASTA 15 PARA LA PAGINACION *--
--* DEL MODULO TUBDER03A.                   *--
--* CONSULTOR3 - 03/03/2016.              *--
--*---------------------------------------*--

RETURN number AS

vcount number;

begin

select count(*) into vcount 
from TUBDER03A
WHERE TO_CHAR(ANOCAL) || TO_CHAR(MESCAL) || TO_CHAR(SEMCAL) like '%'||pbusqueda||'%';
return vcount;
end;