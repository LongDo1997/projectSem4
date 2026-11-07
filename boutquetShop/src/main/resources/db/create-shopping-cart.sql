-- Add missing shopping-cart tables without changing existing data.
SET XACT_ABORT ON;
BEGIN TRANSACTION;
IF OBJECT_ID(N'dbo.Carts', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Carts (
        Id INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_Carts PRIMARY KEY,
        UserId INT NOT NULL,
        CreatedAt DATETIME NOT NULL CONSTRAINT DF_Carts_CreatedAt DEFAULT GETDATE(),
        CONSTRAINT UQ_Carts_UserId UNIQUE (UserId),
        CONSTRAINT FK_Carts_Users FOREIGN KEY (UserId) REFERENCES dbo.Users(Id)
    );
END;
IF OBJECT_ID(N'dbo.CartItems', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.CartItems (
        Id INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_CartItems PRIMARY KEY,
        CartId INT NOT NULL,
        BouquetId INT NOT NULL,
        Quantity INT NOT NULL,
        UnitPrice DECIMAL(18,2) NOT NULL,
        CONSTRAINT UQ_CartItems_Cart_Bouquet UNIQUE (CartId, BouquetId),
        CONSTRAINT CK_CartItems_Quantity CHECK (Quantity > 0),
        CONSTRAINT FK_CartItems_Carts FOREIGN KEY (CartId) REFERENCES dbo.Carts(Id),
        CONSTRAINT FK_CartItems_Bouquets FOREIGN KEY (BouquetId) REFERENCES dbo.Bouquets(Id)
    );
END;
COMMIT TRANSACTION;