alter table eg_sr_service  
add column if not exists tickettype character varying(256),
add column if not exists ticketsubtype character varying(256);


alter table eg_sr_service 
alter column servicecode drop not null ;