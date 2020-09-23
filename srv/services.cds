    using { sap.capire.bookstore2 as db } from '../db/schema';

    // Define Books Service
    service BooksService {
        @readonly entity Books as projection   on db.Books { *, category as genre } excluding { category, createdBy, createdAt, modifiedBy, modifiedAt };
        @readonly entity Authors as projection on db.Authors;
    }

    // Define Orders Service
    service OrdersService {
        entity Orders as projection on db.Orders;
        // OrderItems are auto exposed
        entity OrderItems as projection on db.OrderItems;
    }

    // Reuse Admin Service
    using { AdminService } from '@sap/capire-products2';
    extend service AdminService with {
        entity Authors as projection on db.Authors;
    }