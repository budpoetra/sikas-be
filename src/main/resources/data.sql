-- Default data for MasterUserType
INSERT INTO MasterUserType (UserType, CreatedDate, UpdatedDate)
VALUES
    ('super-admin', GETDATE(), GETDATE()),
    ('owner', GETDATE(), GETDATE()),
    ('admin', GETDATE(), GETDATE()),
    ('cashier', GETDATE(), GETDATE());

-- Default data for MasterFeature
INSERT INTO MasterFeature (Name, CreatedDate, UpdatedDate)
VALUES
    ('user', GETDATE(), GETDATE()),
    ('product', GETDATE(), GETDATE()),
    ('product-category', GETDATE(), GETDATE()),
    ('product-delivery', GETDATE(), GETDATE()),
    ('barcode', GETDATE(), GETDATE()),
    ('transaction', GETDATE(), GETDATE()),
    ('report', GETDATE(), GETDATE());

-- Default data for MasterAction
INSERT INTO MasterAction (Name, CreatedDate, UpdatedDate)
VALUES
    ('create', GETDATE(), GETDATE()),
    ('read', GETDATE(), GETDATE()),
    ('update', GETDATE(), GETDATE()),
    ('delete', GETDATE(), GETDATE());

-- Default data for UserTypePermission
INSERT INTO UserTypePermission (UserTypeId, FeatureId, ActionId, IsAllowed, CreatedDate, UpdatedDate)
VALUES
    (2, 1, 1, 1, GETDATE(), GETDATE()), -- owner can create user
    (2, 1, 2, 1, GETDATE(), GETDATE()), -- owner can read user
    (2, 1, 3, 1, GETDATE(), GETDATE()), -- owner can update user
    (2, 1, 4, 1, GETDATE(), GETDATE()), -- owner can delete user
    (2, 2, 1, 1, GETDATE(), GETDATE()), -- owner can create product
    (2, 2, 2, 1, GETDATE(), GETDATE()), -- owner can read product
    (2, 2, 3, 1, GETDATE(), GETDATE()), -- owner can update product
    (2, 2, 4, 1, GETDATE(), GETDATE()), -- owner can delete product
    (2, 3, 1, 1, GETDATE(), GETDATE()), -- owner can create product-category
    (2, 3, 2, 1, GETDATE(), GETDATE()), -- owner can read product-category
    (2, 3, 3, 1, GETDATE(), GETDATE()), -- owner can update product-category
    (2, 3, 4, 1, GETDATE(), GETDATE()), -- owner can delete product-category
    (2, 4, 1, 1, GETDATE(), GETDATE()), -- owner can create product-delivery
    (2, 4, 2, 1, GETDATE(), GETDATE()), -- owner can read product-delivery
    (2, 5, 1, 1, GETDATE(), GETDATE()), -- owner can create barcode
    (2, 5, 2, 1, GETDATE(), GETDATE()), -- owner can read barcode
    (2, 5, 3, 1, GETDATE(), GETDATE()), -- owner can update barcode
    (2, 5, 4, 1, GETDATE(), GETDATE()), -- owner can delete barcode
    (2, 6, 1, 1, GETDATE(), GETDATE()), -- owner can create transaction
    (2, 6, 2, 1, GETDATE(), GETDATE()), -- owner can read transaction
    (2, 6, 3, 1, GETDATE(), GETDATE()), -- owner can update transaction
    (2, 6, 4, 1, GETDATE(), GETDATE()), -- owner can delete transaction
    (2, 7, 1, 1, GETDATE(), GETDATE()), -- owner can create report
    (2, 7, 2, 1, GETDATE(), GETDATE()), -- owner can read report
    (2, 7, 3, 1, GETDATE(), GETDATE()), -- owner can update report
    (2, 7, 4, 1, GETDATE(), GETDATE()), -- owner can delete report
    (3, 1, 2, 1, GETDATE(), GETDATE()), -- admin can read user
    (3, 2, 1, 1, GETDATE(), GETDATE()), -- admin can create product
    (3, 2, 2, 1, GETDATE(), GETDATE()), -- admin can read product
    (3, 2, 3, 1, GETDATE(), GETDATE()), -- admin can update product
    (3, 2, 4, 1, GETDATE(), GETDATE()), -- admin can delete product
    (3, 3, 1, 1, GETDATE(), GETDATE()), -- admin can create product-category
    (3, 3, 2, 1, GETDATE(), GETDATE()), -- admin can read product-category
    (3, 3, 3, 1, GETDATE(), GETDATE()), -- admin can update product-category
    (3, 4, 1, 1, GETDATE(), GETDATE()), -- admin can create product-delivery
    (3, 4, 2, 1, GETDATE(), GETDATE()), -- admin can read product-delivery
    (3, 4, 3, 1, GETDATE(), GETDATE()), -- admin can update product-delivery
    (3, 5, 1, 1, GETDATE(), GETDATE()), -- admin can create barcode
    (3, 5, 2, 1, GETDATE(), GETDATE()), -- admin can read barcode
    (3, 5, 3, 1, GETDATE(), GETDATE()), -- admin can update barcode
    (4, 2, 2, 1, GETDATE(), GETDATE()), -- cashier can read product
    (4, 3, 2, 1, GETDATE(), GETDATE()), -- cashier can read product-category
    (4, 6, 1, 1, GETDATE(), GETDATE()), -- cashier can create transaction
    (4, 6, 2, 1, GETDATE(), GETDATE()), -- cashier can read transaction
    (4, 6, 3, 1, GETDATE(), GETDATE()); -- cashier can update transaction

-- Default data for ProductCategory
INSERT INTO MasterProductCategories (Category, CreatedDate, UpdatedDate, CreatedBy, UpdatedBy)
VALUES
    ('Food', GETDATE(), GETDATE(), 3, 3),
    ('Drink', GETDATE(), GETDATE(), 3, 3);

-- Default data for Product
INSERT INTO MasterProducts (CategoryId, ProductName, ProductCode, Barcode, Price, Stock, Status, CreatedDate, UpdatedDate, CreatedBy, UpdatedBy)
VALUES
    (1, 'Indomie Rebus', 'INDR', '1234567890123', 8000, 150, 1, GETDATE(), GETDATE(), 3, 3),
    (1, 'Indomie Goreng', 'INDG', '1234567890124', 9000, 100, 1, GETDATE(), GETDATE(), 3, 3),
    (2, 'Teh Botol', 'TEHB', '1234567890125', 5000, 200, 1, GETDATE(), GETDATE(), 3, 3),
    (2, 'Aqua 600ml', 'AQ6M', '1234567890126', 3000, 250, 1, GETDATE(), GETDATE(), 3, 3);