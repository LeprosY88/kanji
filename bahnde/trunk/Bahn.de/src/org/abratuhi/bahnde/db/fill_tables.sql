insert into stations("id", "name", "duration", "coordinates") values (1, 'Essen', 10, '-19;2');
insert into stations("id", "name", "duration", "coordinates") values (2, 'Bochum', 7, '0;0');


insert into incident_stations("id", "id_start", "id_end") values (1, 1, 2);
insert into incident_stations("id", "id_start", "id_end") values (2, 2, 1);


insert into routes("id", "edge_id", "start", "duration", "type") values (1, 1, '2011-01-17 12:05', 30, 'RE');
insert into routes("id", "edge_id", "start", "duration", "type") values (2, 1, '2011-01-17 12:37', 30, 'RE');
insert into routes("id", "edge_id", "start", "duration", "type") values (3, 1, '2011-01-17 13:05', 30, 'RE');