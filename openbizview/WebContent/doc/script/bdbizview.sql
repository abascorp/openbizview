prompt Creando el TableSpace default: <dbbizview>
prompt Introduzca el RUTA\NOMBRE del DataFile EJEMPLO: <C:\ORACLE\PRODUCT\10.2.0\ORADATA\ORCL\DBBIZVIEW01.DBF>
create tablespace bdbizview 
       datafile '&1'    
       size 300M
default storage (initial 256K
                 next 256k
                 pctincrease   0);

/
