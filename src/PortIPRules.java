import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;

public class PortIPRules {

    Set<Integer> portSet;
    Set<String> IPSet;

    // using a TreeSet for storing ranges to allow O(log n) lookups - uses compareTo to maintain sorted order
    TreeSet<PortRange> portRanges;
    TreeSet<OctetRange> IPRanges;

    public PortIPRules() {
        portSet = new HashSet<>(); IPSet = new HashSet<>();
        portRanges = new TreeSet<>(); IPRanges = new TreeSet<>();
    }

    public void addIPRule(String IP) {
        if (IP.contains("-")) { // range
            String[] split = IP.split("-");
            IPRanges.add(new OctetRange(split[0], split[1]));
        } else
            IPSet.add(IP);
    }

    public void addPortRule(String port) {
        if (port.contains("-")) { // range
            String[] split = port.split("-");
            portRanges.add(new PortRange(split[0], split[1]));
        } else
            portSet.add(Integer.parseInt(port));
    }

    public boolean match(String port, String IP) {
        return matchPort(port) && matchIP(IP);
    }

    private boolean matchPort(String port) {
        if (portSet.contains(Integer.parseInt(port)))
            return true;
        PortRange range = new PortRange(port, port);
        if (portRanges.contains(range)) // start boundary match
            return true;
        boolean flag = false;
        PortRange lower = portRanges.lower(range);
        if (lower != null)
            flag = lower.end >= range.start; // falls within end of left range
        return flag;
    }

    private boolean matchIP(String IP) {
        if (IPSet.contains(IP))
            return true;
        OctetRange range = new OctetRange(IP, IP);
        if (IPRanges.contains(range)) // start boundary match
            return true;
        boolean flag = false;
        OctetRange lower = IPRanges.lower(range);
        if (lower != null)
            flag = lower.endIP >= range.startIP; // falls within end of left range
        return flag;
    }
}
