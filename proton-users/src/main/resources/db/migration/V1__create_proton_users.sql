create table "USERS" (
    "ID" UUID NOT NULL PRIMARY KEY,
    "NAME" VARCHAR NOT NULL,
    "ORG_ID" UUID
);

create table "GROUPS" (
    "ID" UUID NOT NULL PRIMARY KEY,
    "NAME" VARCHAR NOT NULL
);

create table "USERS_GROUPS" (
    "USER" UUID NOT NULL,
    "GROUP" UUID NOT NULL
);

create table "ORGANIZATIONS" (
    "ID" UUID NOT NULL PRIMARY KEY,
    "NAME" VARCHAR NOT NULL
);

create table "RESOURCES" (
    "ID" UUID NOT NULL PRIMARY KEY,
    "TYPE" UUID NOT NULL,
    "PROPS_HSTORE" hstore NOT NULL
);

alter table "USERS"
add constraint "ORG_FK"
foreign key("ORG_ID")
references "ORGANIZATIONS"("ID")
on update RESTRICT
on delete NO ACTION;

alter table "USERS_GROUPS"
add constraint "USERS_FK"
foreign key("USER")
references "USERS"("ID")
on update RESTRICT
on delete NO ACTION;

alter table "USERS_GROUPS"
add constraint "GROUPS_FK"
foreign key("GROUP")
references "GROUPS"("ID")
on update RESTRICT
on delete NO ACTION;