INSERT INTO tpp_ref_account_type (value)
VALUES ('Клиентский'),
       ('Внутрибанковский');

INSERT INTO tpp_ref_product_class (value, gbi_code, gbi_name, product_row_code, product_row_name, subclass_code, subclass_name)
VALUES ('03.012.002', '03', 'Розничный бизнес', '012', 'Драг. металлы', '002', 'Хранение'),
       ('02.001.005', '02', 'Розничный бизнес', '001', 'Сырье', '005', 'Продажа');

INSERT INTO tpp_ref_product_register_type (value
                                           , register_type_name
                                           , product_class_code
                                           , account_type)
VALUES ('03.012.002_47533_ComSoLd', 'Хранение ДМ.', '03.012.002', 'Клиентский'),
       ('02.001.005_45343_CoDowFF', 'Серебро. Выкуп.', '02.001.005', 'Клиентский');
--        ('RKO', 'Расчетный счет', 'RKO', TIMESTAMP '2023-12-03 00:00:00', 'CLIENT_ACCOUNT');

INSERT INTO account_pool (branch_code
                          , currency_code
                          , mdm_code
                          , priority_code
                          , registry_type_code)
VALUES ('0022', '800', '15', '00', '03.012.002_47533_ComSoLd'),
       ('0021', '500', '13', '00', '02.001.005_45343_CoDowFF');

INSERT INTO account(account_pool_id, account_number, bussy)
SELECT id, '475335516415314841861', false
FROM account_pool
WHERE registry_type_code = '03.012.002_47533_ComSoLd';

INSERT INTO account(account_pool_id, account_number, bussy)
SELECT id, '4753321651354151', false
FROM account_pool
WHERE registry_type_code = '03.012.002_47533_ComSoLd';

INSERT INTO account(account_pool_id, account_number, bussy)
SELECT id, '4753352543276345', false
FROM account_pool
WHERE registry_type_code = '03.012.002_47533_ComSoLd';

INSERT INTO account(account_pool_id, account_number, bussy)
SELECT id, '453432352436453276', false
FROM account_pool
WHERE registry_type_code = '02.001.005_45343_CoDowFF';

INSERT INTO account(account_pool_id, account_number, bussy)
SELECT id, '45343221651354151', false
FROM account_pool
WHERE registry_type_code = '02.001.005_45343_CoDowFF';

INSERT INTO account(account_pool_id, account_number, bussy)
SELECT id, '4534352543276345', false
FROM account_pool
WHERE registry_type_code = '02.001.005_45343_CoDowFF';

INSERT INTO tpp_product (product_code_id, client_id, type, number, priority, date_of_conclusion, start_date_time, end_date_time, days, penalty_rate, nso, threshold_amount, requisite_type, interest_rate_type, tax_rate, reasone_close, state)
VALUES (1, 1001, '03.012.002_47533_ComSoLd', 'P001', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 30, 0.05, 0.02, 10000.00, 'Requisite Type 1', 'Interest Rate Type 1', 0.10, 'Reason for Close 1', 'Active'),
       (2, 1002, '02.001.005_45343_CoDowFF', 'P002', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 60, 0.03, 0.01, 5000.00, 'Requisite Type 2', 'Interest Rate Type 2', 0.08, 'Reason for Close 2', 'Inactive');

INSERT INTO agreement (product_id, general_agreement_id, supplementary_agreement_id, arrangement_type, sheduler_job_id, number,
                      opening_date, closing_date, cancel_date, validity_duration,
                      cancellation_reason,status , interest_calculation_date , interest_rate , coefficient , coefficient_action ,
                      minimum_interest_rate , minimum_interest_rate_coefficient , minimum_interest_rate_coefficient_action ,
                      maximal_interest_rate , maximal_interest_rate_coefficient , maximal_interest_rate_coefficient_action)
VALUES (1,'GAI001','SAI001','ArrangementType1',1,'Agreement001',
        CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,
        365,'Cancellation Reason 1','Active',
        CURRENT_TIMESTAMP ,0.07 ,0.05,'Action1',
        0.10 ,0.08 ,'Action2',
        0.15 ,0.12 ,'Action3'),
       (2,'GAI002','SAI002','ArrangementType2',2,'Agreement002',
        CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,
        730,'Cancellation Reason 2','Inactive',
        CURRENT_TIMESTAMP ,0.05 ,0.03,'Action4',
        0.08 ,0.06 ,'Action5',
        0.12 ,0.10 ,'Action6');