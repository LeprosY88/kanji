create table stations(
"id" integer generated always as identity primary key,
"name" varchar(255) not null,
"duration" integer,
"coordinates" varchar(255) not null
);

create table incident_stations(
"id" integer generated always as identity primary key,
"id_start" integer not null,
"id_end" integer not null
);

create table routes(
"id"integer generated always as identity primary key,
"edge_id" integer references incident_stations("id"),
"start" varchar(255) not null,
"duration" integer not null
);