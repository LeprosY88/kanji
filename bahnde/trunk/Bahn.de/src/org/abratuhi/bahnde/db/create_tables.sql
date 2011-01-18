create table stations(
`id` integer auto_increment primary key,
`name` varchar(255) not null,
`duration` integer,
`coordinates` varchar(255) not null
);

create table incident_stations(
`id` integer auto_increment primary key,
`id_start` integer not null,
`id_end` integer not null
)

create table routes(
`id` integer auto_increment references incident_stations(`id`),
`start` datetime not null,
`duration` integer not null
)