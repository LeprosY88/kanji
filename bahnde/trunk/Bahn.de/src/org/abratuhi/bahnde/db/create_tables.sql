create table stations(
"id" integer primary key,
"name" varchar(255) not null,
"duration" integer,
"coordinates" varchar(255) not null
);

create table incident_stations(
"id" integer primary key,
"id_start" integer references stations("id"),
"id_end" integer references stations("id")
);

create table routes(
"id" integer primary key,
"edge_id" integer references incident_stations("id"),
"start" varchar(255) not null,
"duration" integer not null,
"type" varchar(3) not null
);