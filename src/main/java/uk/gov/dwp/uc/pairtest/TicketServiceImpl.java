package uk.gov.dwp.uc.pairtest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

public class TicketServiceImpl implements TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    private final TicketPaymentService paymentService;
    private final SeatReservationService reservationService;

    public TicketServiceImpl(TicketPaymentService paymentService, SeatReservationService reservationService) {
        this.paymentService = paymentService;
        this.reservationService = reservationService;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) {
        // Validate input
        if (accountId <= 0) {
            throw new IllegalArgumentException("Invalid account ID.");
        }

        int totalTickets = 0;
        int adultTickets = 0;
        int childTickets = 0;
        int infantTickets = 0;
        int totalAmount = 0;

        for (TicketTypeRequest request : ticketTypeRequests) {
            switch (request.getTicketType()) {
                case ADULT:
                    adultTickets += request.getNoOfTickets();
                    totalAmount += request.getNoOfTickets() * 25;
                    break;
                case CHILD:
                    childTickets += request.getNoOfTickets();
                    totalAmount += request.getNoOfTickets() * 15;
                    break;
                case INFANT:
                    infantTickets += request.getNoOfTickets();
                    break;
            }
            totalTickets += request.getNoOfTickets();
        }

        if (totalTickets > 25) {
            throw new IllegalArgumentException("Cannot purchase more than 25 tickets at a time.");
        }

        if (adultTickets == 0 && (childTickets > 0 || infantTickets > 0)) {
            throw new IllegalArgumentException("Child and Infant tickets cannot be purchased without an Adult ticket.");
        }

        // Process payment
        paymentService.makePayment(accountId, totalAmount);
        logger.info("Payment of £{} made for account ID {}", totalAmount, accountId);

        // Reserve seats
        int seatsToReserve = adultTickets + childTickets;
        reservationService.reserveSeat(accountId, seatsToReserve);
        logger.info("Reserved {} seats for account ID {}", seatsToReserve, accountId);
    }

}
