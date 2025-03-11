
import org.mockito.Mockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TicketServiceImplTest {

    private TicketServiceImpl ticketService;
    private TicketPaymentService paymentService;
    private SeatReservationService reservationService;

    @BeforeEach
    public void setUp() {
        paymentService = Mockito.mock(TicketPaymentService.class);
        reservationService = Mockito.mock(SeatReservationService.class);
        ticketService = new TicketServiceImpl(paymentService, reservationService);
    }

    @Test
    public void testPurchaseTickets_Success() {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest childRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        TicketTypeRequest infantRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 0);

        ticketService.purchaseTickets(1L, adultRequest, childRequest, infantRequest);

        Mockito.verify(paymentService).makePayment(1L, 65);
        Mockito.verify(reservationService).reserveSeat(1L, 3);
    }

    @Test
    public void testPurchaseTickets_InvalidAccount() {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);

        assertThrows(IllegalArgumentException.class, () -> {
            ticketService.purchaseTickets(0L, adultRequest);
        });
    }

    @Test
    public void testPurchaseTickets_ExceedsTicketLimit() {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 26);

        assertThrows(IllegalArgumentException.class, () -> {
            ticketService.purchaseTickets(1L, adultRequest);
        });
    }

    @Test
    public void testPurchaseTickets_NoAdultTicket() {
        TicketTypeRequest childRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 0);

        assertThrows(IllegalArgumentException.class, () -> {
            ticketService.purchaseTickets(1L, childRequest);
        });
    }
}
