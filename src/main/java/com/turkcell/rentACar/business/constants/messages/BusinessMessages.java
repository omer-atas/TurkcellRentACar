package com.turkcell.rentACar.business.constants.messages;


public class BusinessMessages {

    //Additional Service
    public static final String ADDITIONAL_SERVICE_ADD = "AdditionalService added : ";
    public static final String ADDITIONAL_SERVICE_UPDATE = " updated..";
    public static final String ADDITIONAL_SERVICE_DELETE = " deleted..";
    public static final String ADDITIONAL_SERVICE_GET_BY_ID = "Success";
    public static final String ADDITIONAL_SERVICE_NOT_FOUND = "There is no data in the id sent";
    public static final String ADDITIONAL_SERVİCE_GET_ALL = "AdditionalServices Listed Successfully";
    public static final String ADDITIONAL_SERVİCE_GET_ALL_PAGED = "AdditionalServices Listed Page Successfully";
    public static final String ADDITIONAL_SERVİCE_GET_ALL_SORTED = "AdditionalServices Listed Sorted Successfully";
    public static final String IS_THERE_A_ADDITIONAL_SERVİCE_OF_ORDERED_ADDITIONAL_SERVICES_AVALIABLE="There are rentals ordered that have this additional service. So the additional service cannot be deleted";

    //Brand
    public static final String BRAND_ADD = "Brand added : ";
    public static final String BRAND_UPDATE = " updated..";
    public static final String BRAND_NAME_NOT_DUPLICATED = "Brand names can't be the same";
    public static final String BRAND_DELETE = " deleted..";
    public static final String BRAND_GET_BY_ID = "Success";
    public static final String BRAND_NOT_FOUND = "There is no brand in the id sent";
    public static final String BRAND_GET_ALL = "Brands Listed Successfully";
    public static final String BRAND_GET_ALL_PAGED = "Brands Listed Page Successfully";
    public static final String BRAND_GET_ALL_SORTED = "Brands Listed Sorted Successfully";
    public static final String IS_THERE_A_BRAND_OF_CAR_AVAILABLE = "There is a car brand corresponding to this brand. Therefore, the deletion cannot be performed...";

    //Car Crash Information
    public static final String CAR_CRASH_INFORMATION_ADD = "Car Crash Information added : ";
    public static final String CAR_CRASH_INFORMATION_UPDATE = " updated..";
    public static final String CAR_CRASH_INFORMATION_DELETE = " deleted..";
    public static final String CAR_CRASH_INFORMATION_GET_BY_ID = "Success";
    public static final String CAR_CRASH_INFORMATION_NOT_FOUND = "There is no car crash in the id sent";
    public static final String CAR_CRASH_INFORMATION_GET_ALL = "Car Crash Informations Listed Successfully";
    public static final String CAR_CRASH_INFORMATION_GET_ALL_PAGED = "Car Crash Informations Listed Page Successfully";
    public static final String CAR_CRASH_INFORMATION_GET_ALL_SORTED = "Car Crash Informations Listed Sorted Successfully";

    // CarMaintenance
    public static final String CAR_MAINTENANCE_ADD = "CarMaintenance added : ";
    public static final String CAR_MAINTENANCE__UPDATE = " updated..";
    public static final String CAR_MAINTENANCE_DELETE = " deleted..";
    public static final String CAR_MAINTENANCE_GET_BY_ID = "Success";
    public static final String CAR_MAINTENANCE_CAR_RENTED = "The car cannot be sent for maintenance because it is on rent.";
    public static final String CAR_MAINTENANCE_CAR_RENTED_NULL_DATE = "The car cannot be sent for maintenance because it is on rent. / null end date.";
    public static final String CAR_MAINTENANCE_NOT_FOUND = "There is no Car Maintenance in the id sent";
    public static final String CAR_MAINTENANCE_GET_ALL = "CarMaintenances Listed Successfully";
    public static final String CAR_MAINTENANCE_GET_ALL_PAGED = "CarMaintenances Listed Page Successfully";
    public static final String CAR_MAINTENANCE_GET_ALL_SORTED = "CarMaintenances Listed Sorted Successfully";
    public static final String CAR_MAINTENANCE_CAR_NOT_FOUND = "There is no Car Maintenance Car in the id sent";

    //Car
    public static final String CAR_ADD = "Car added : ";
    public static final String CAR_UPDATE = " updated..";
    public static final String CAR_DELETE = " deleted..";
    public static final String CAR_GET_BY_ID = "Success";
    public static final String CAR_NOT_FOUND = "There is no car crash in the id sent";
    public static final String CAR_GET_ALL = "Cars Listed Successfully";
    public static final String CAR_GET_ALL_PAGED = "Cars Listed Page Successfully";
    public static final String CAR_GET_ALL_SORTED = "Cars Listed Sorted Successfully";
    public static final String CAR_FIND_BY_DAILY_PRICE_LESS_THAN_EQUAL_NOT_FOUND = "findByDailyPriceLessThanEqual not list";
    public static final String CAR_FIND_BY_DAILY_PRICE_LESS_THAN_EQUAL = "findByDailyPriceLessThanEqual Listed Sorted Successfully";
    public static final String IS_THERE_A_CAR_OF_CAR_CRASH_INFORMATION_AVAILABLE = "There is damage information of this car. Therefore, it cannot be deleted. (Damage records of the car must be deleted first.)";
    public static final String IS_THERE_A_CAR_OF_RENT_AVAILABLE ="There is a rental for this car. Therefore it cannot be deleted. (Leases belonging to the car must be deleted first)";
    public static final String IS_THERE_A_CAR_OF_CAR_MAINTENANCE_AVAILABLE ="There are maintenances for this car. Therefore, it cannot be deleted. (Car maintenance must be deleted first)";
    public static final String CARS_WITH_CAR_COLOR_SENT_ID ="Cars with specific color are listed";
    public static final String CARS_WITH_CAR_BRAND_SENT_ID  ="Cars with car brand sent id are listed";

    //Color
    public static final String COLOR_ADD = "Color added : ";
    public static final String COLOR_UPDATE = " updated..";
    public static final String COLOR_NAME_NOT_DUPLICATED = "Color names can't be the same";
    public static final String COLOR_DELETE = " deleted..";
    public static final String COLOR_GET_BY_ID = "Success";
    public static final String COLOR_NOT_FOUND = "There is no color in the id sent";
    public static final String COLOR_GET_ALL = "Colors Listed Successfully";
    public static final String COLOR_GET_ALL_PAGED = "Colors Listed Page Successfully";
    public static final String COLOR_GET_ALL_SORTED = "Colors Listed Sorted Successfully";
    public static final String IS_THERE_A_COLOR_OF_CAR_AVAILABLE = "There are cars with this color. Therefore, the deletion cannot be performed...";

    // CorporateCustomer
    public static final String CORPORATE_CUSTOMER_ADD = "CorporateCustomer added : ";
    public static final String CORPORATE_CUSTOMER_UPDATE = " updated..";
    public static final String CORPORATE_CUSTOMER_TAX_NUMBER_NOT_DUPLICATED = "Tax number names can't be the same";
    public static final String CORPORATE_CUSTOMER_TAX_NUMBER_REGEX = "Enter the correct ID number.";
    public static final String CORPORATE_CUSTOMER_DELETE = " deleted..";
    public static final String CORPORATE_CUSTOMER_GET_BY_ID = "Success";
    public static final String CORPORATE_CUSTOMER_NOT_FOUND = "There is no CorporateCustomer in the id sent";
    public static final String CORPORATE_CUSTOMER_GET_ALL = "CorporateCustomers Listed Successfully";
    public static final String CORPORATE_CUSTOMER_GET_ALL_PAGED = "CorporateCustomers Listed Page Successfully";
    public static final String CORPORATE_CUSTOMER_GET_ALL_SORTED = "CorporateCustomers Listed Sorted Successfully";

    // IndividuaLCustomer
    public static final String INDIVIDUAL_CUSTOMER_ADD = "IndividuaLCustomer added : ";
    public static final String INDIVIDUAL_CUSTOMER_UPDATE = " updated..";
    public static final String INDIVIDUAL_CUSTOMER_NATIONAL_IDENTITY_NOT_DUPLICATED = "National Identity names can't be the same";
    public static final String INDIVIDUAL_CUSTOMER_NATIONAL_IDENTITY_REGEX = "Enter the correct ID number.";
    public static final String INDIVIDUAL_CUSTOMER_DELETE = " deleted..";
    public static final String INDIVIDUAL_CUSTOMER_GET_BY_ID = "Success";
    public static final String INDIVIDUAL_CUSTOMER_NOT_FOUND = "There is no IndividuaLCustomer in the id sent";
    public static final String INDIVIDUAL_CUSTOMER_GET_ALL = "IndividuaLCustomers Listed Successfully";
    public static final String INDIVIDUAL_CUSTOMER_GET_ALL_PAGED = "IndividuaLCustomers Listed Page Successfully";
    public static final String INDIVIDUAL_CUSTOMER_GET_ALL_SORTED = "IndividuaLCustomers Listed Sorted Successfully";

    // Invoice
    public static final String INVOICE_UPDATE = " updated..";
    public static final String INVOICE_RENT_RETURN = "The invoice could not be issued because the rental return information was not entered.";
    public static final String INVOICE_N0_NOT_DUPLICATED = "It has the invoice number. Give another name";
    public static final String INVOICE_DELETE = " deleted..";
    public static final String INVOICE_GET_BY_ID = "Success";
    public static final String INVOICE_GET_BY_RENT_ID = "Success";
    public static final String INVOICE_NOT_FOUND = "There is no Invoice in the id sent";
    public static final String INVOICE_RENT_NOT_FOUND = "There is no Invoice in the id sent";
    public static final String INVOICE_CUSTOMER = "Success";
    public static final String INVOICE_CUSTOMER_NOT_FOUND = "There is no invoice record of the customer belonging to the sent id";
    public static final String INVOICE_GET_ALL = "Invoices Listed Successfully";
    public static final String INVOICE_GET_ALL_PAGED = "Invoices Listed Page Successfully";
    public static final String INVOICE_GET_ALL_SORTED = "Invoices Listed Sorted Successfully";
    public static final String INVOICE_IN_SPECIFIC_DATE_RANGE = "There is no invoice record in the sent range.";

    //OrderedAdditionalService
    public static final String ORDERED_ADDITIONAL_SERVICE_UPDATE = " updated..";
    public static final String ORDERED_ADDITIONAL_SERVICE_DELETE = " deleted..";
    public static final String ORDERED_ADDITIONAL_SERVICE_GET_BY_ID = "Success";
    public static final String ORDERED_ADDITIONAL_SERVICE_NOT_FOUND = "There is no OrderedAdditionalService in the id sent";
    public static final String ORDERED_ADDITIONAL_SERVICE_RENT_NOT_FOUND = "There is no OrderedAdditionalServiceRent in the id sent";
    public static final String ORDERED_ADDITIONAL_SERVICE_GET_ALL = "OrderedAdditionalServices Listed Successfully";
    public static final String ORDERED_ADDITIONAL_SERVICE_GET_ALL_PAGED = "OrderedAdditionalServices Listed Page Successfully";
    public static final String ORDERED_ADDITIONAL_SERVICE_GET_ALL_SORTED = "OrderedAdditionalServices Listed Sorted Successfully";

    // Payment
    public static final String PAYMENT_ADD = "Payment Added Successfully ";
    public static final String PAYMENT_UPDATE = " updated..";
    public static final String PAYMENT_DELETE = " deleted..";
    public static final String PAYMENT_GET_BY_ID = "Success";
    public static final String PAYMENT_NOT_FOUND = "There is no payment in the id sent";
    public static final String PAYMENT_GET_ALL = "Payments Listed Successfully";
    public static final String PAYMENT_CAN_NOT_MAKE_PAYMENT = "Failed to receive payment to perform the lease.";
    public static final String PAYMENT_EXİSTS_BY_INVOICE = "This invoice has a payment";

    //Rent
    public static final String RENT_END_DATE_BEFORE_STARTING_DATE = "End date cannot be earlier than start date";
    public static final String RENT_UPDATE = " updated..";
    public static final String RENT_DELETE = " deleted..";
    public static final String RENT_GET_BY_ID = "Success";
    public static final String RENT_NOT_FOUND = "There is no Rent in the id sent";
    public static final String RENT_GET_ALL = "Rents Listed Successfully";
    public static final String RENT_GET_ALL_PAGED = "Rents Listed Page Successfully";
    public static final String RENT_GET_ALL_SORTED = "Rents Listed Sorted Successfully";
    public static final String RENT_CAR_MAINTENANCE = "This car cannot be rented as it is under maintenance.";
    public static final String RENT_CAR_MAINTENANCE_NULL_DATE = "This car cannot be rented as it is under maintenance. / return date equals null";
    public static final String CUSTOMER_NOT_FOUND ="The customer with this ID is not available";
    public static final String RENT_TO_CITY_NOT_FOUND ="The city with this ID is not available - toCity";
    public static final String RENT_FROM_CITY_NOT_FOUND ="The city with this ID is not available - fromCity";
    public static final String RENT_DELAY_END_DATE_FOR_INDIVIDUAL_CUSTOMER= "Necessary structures have been created for individual customer late delivery.";
    public static final String RENT_DELAY_END_DATE_FOR_CORPORATE_CUSTOMER="Necessary structures have been created for corporate customer late delivery.";
    public static final String IS_THE_INVOICE_AVAILABLE_FOR_THIS_RENTAL = "There is an invoice corresponding to this rental. Therefore, the lease cannot be deleted";
    public static final String IS_THE_ORDERED_ADDITIONAL_SERVICE_AVAILABLE_FOR_THIS_RENTAL = "There are additional services ordered corresponding to this lease. Therefore, the lease cannot be deleted";
    public static final String RENT_DELAY_END_DATE_CHECK = "The return date is the same as the system";

    //Credit Card Information
    public static final String CREDIT_CARD_INFORMATION_ADD = "Credit Card Added Successfully : ";
    public static final String CREDIT_CARD_INFORMATION_NOT_FOUND = "Credit Card Not Found";
    public static final String CREDIT_CARD_GET_ALL = "Credit card Listed Successfully";
    public static final String CREDIT_CARD_INFORMATION_GET_BY_ID = "Credit card information brought";
    public static final String CUSTOMER_CREDIT_CARD_INFORMATION_NOT_FOUND = "Customer credit card not found";
    public static final String CUSTOMER_CREDIT_CARD_INFORMATION_LISTED = "Customer credit cards listed";
    public static final String CREDIT_CARD_INFORMATION_DELETE = "Credit Cart Delted";
    public static final String CARD_NO_REGEX_MESSAGE = "Not in credit card number format";
    public static final String CARD_HOLDER_REGEX_MESSAGE = "Enter the correct credit card holder.";

    //User
    public static final String USER_EMAİL_EXİSTS = "There is a user with this email";



}
