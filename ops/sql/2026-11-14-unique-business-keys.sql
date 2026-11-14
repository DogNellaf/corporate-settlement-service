-- PostgreSQL only. Run during a maintenance window, after taking a backup.
-- Does not delete or merge historical data. Any duplicate aborts the whole script.
BEGIN;
LOCK TABLE tpp_product, tpp_product_register, agreement IN SHARE ROW EXCLUSIVE MODE;
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM tpp_product WHERE number IS NOT NULL GROUP BY number HAVING count(*)>1)
       OR EXISTS (SELECT 1 FROM tpp_product_register WHERE product_id IS NOT NULL GROUP BY product_id,type HAVING count(*)>1)
       OR EXISTS (SELECT 1 FROM agreement WHERE number IS NOT NULL GROUP BY number HAVING count(*)>1) THEN
        RAISE EXCEPTION 'Duplicate business keys found. Reconcile historical records before applying unique constraints.';
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conrelid='tpp_product'::regclass AND conname='uq_product_number') THEN
        ALTER TABLE tpp_product ADD CONSTRAINT uq_product_number UNIQUE(number);
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conrelid='tpp_product_register'::regclass AND conname='uq_register_product_type') THEN
        ALTER TABLE tpp_product_register ADD CONSTRAINT uq_register_product_type UNIQUE(product_id,type);
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conrelid='agreement'::regclass AND conname='uq_agreement_number') THEN
        ALTER TABLE agreement ADD CONSTRAINT uq_agreement_number UNIQUE(number);
    END IF;
END $$;
COMMIT;
