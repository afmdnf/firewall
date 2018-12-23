import org.junit.Test;
import static org.junit.Assert.*;

public class FirewallTest {

    Firewall f;
    public FirewallTest() { this.f = new Firewall("src/rules.csv"); }

    @Test
    public void exampleTest() {
        assertTrue(f.accept_packet("inbound", "tcp", 80, "192.168.1.2"));
        assertTrue(f.accept_packet("inbound", "udp", 53, "192.168.2.1"));
        assertTrue(f.accept_packet("outbound", "tcp", 10234, "192.168.10.11"));
        assertFalse(f.accept_packet("inbound", "tcp", 81, "192.168.1.2"));
        assertFalse(f.accept_packet("inbound", "udp", 24, "52.12.48.92"));
    }

    @Test
    public void differentiate() {
        // Firewall can differentiate between direction and protocol for allowed port/IP
        assertFalse(f.accept_packet("outbound", "tcp", 80, "192.168.1.2"));
        assertFalse(f.accept_packet("inbound", "udp", 80, "192.168.1.2"));
    }

    // TARGET RULE for tests below: (inbound,tcp,443-8547,0.0.1.8-255.127.61.44)

    @Test
    public void rangeTest() {
        // Both IP and port ranges
        assertTrue(f.accept_packet("inbound", "tcp", 445, "255.126.67.1"));
        assertTrue(f.accept_packet("inbound", "tcp", 8000, "0.0.3.0"));
        assertFalse(f.accept_packet("inbound", "tcp", 40, "0.0.3.0"));
        assertFalse(f.accept_packet("inbound", "tcp", 445, "255.255.5.5"));
    }

    @Test
    public void portRangeBoundaries() {
        assertTrue(f.accept_packet("inbound", "tcp", 443, "255.126.67.1"));
        assertFalse(f.accept_packet("inbound", "tcp", 442, "255.126.67.1"));
        assertTrue(f.accept_packet("inbound", "tcp", 8547, "255.126.67.1"));
        assertFalse(f.accept_packet("inbound", "tcp", 8548, "255.126.67.1"));
    }

    @Test
    public void IPRangeBoundaries() {
        assertTrue(f.accept_packet("inbound", "tcp", 443, "0.0.1.8"));
        assertFalse(f.accept_packet("inbound", "tcp", 443, "0.0.1.7"));
        assertTrue(f.accept_packet("inbound", "tcp", 443, "255.127.61.44"));
        assertFalse(f.accept_packet("inbound", "tcp", 443, "255.127.62.0"));
    }

    @Test
    public void encompass() {
        Firewall e = new Firewall("src/encompass.csv");
        // encompass all possible ports and all possible IPs
        // TARGET RULE: (inbound,tcp,1-65535,0.0.0.0-255.255.255.255)
        assertTrue(e.accept_packet("inbound", "tcp", 1, "0.0.0.0"));
        assertTrue(e.accept_packet("inbound", "tcp", 65535, "0.0.0.0"));
        assertTrue(e.accept_packet("inbound", "tcp", 1, "255.255.255.255"));
        assertTrue(e.accept_packet("inbound", "tcp", 65535, "255.255.255.255"));
        assertTrue(e.accept_packet("inbound", "tcp", 60000, "250.255.255.255"));
        assertTrue(e.accept_packet("inbound", "tcp", 80, "2.2.2.2"));
        assertFalse(e.accept_packet("inbound", "udp", 65535, "255.255.255.255"));
    }
}
