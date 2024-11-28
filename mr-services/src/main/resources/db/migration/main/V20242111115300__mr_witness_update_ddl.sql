-- Add the 'dateOfBirth' column to the 'public.eg_mr_witness' table
ALTER TABLE public.eg_mr_witness 
ADD COLUMN IF NOT EXISTS  dateofbirth int8;

-------------------------------------------------------

-- Add the 'dateOfBirth' column to the 'public.eg_mr_witness_audit' table
ALTER TABLE public.eg_mr_witness_audit 
ADD COLUMN IF NOT EXISTS  dateofbirth int8;
