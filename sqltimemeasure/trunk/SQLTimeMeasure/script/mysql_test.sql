#driver com.mysql.jdbc.Driver
#url jdbc:mysql://localhost:3306/test
#user root
#password password
#begin measure test
#begin loop 10000
!select * from test
!select id from test
#end loop
#end measure