import java.util.Objects;

public class OctetRange implements Comparable<OctetRange> {
    int startA, startB, startC, startD;
    int endA, endB, endC, endD;
    Long startIP; Long endIP;

    public OctetRange(String startIP, String endIP) {
        int[] start = processIP(startIP);
        int[] end = processIP(endIP);
        startA = start[0]; startB = start[1]; startC = start[2]; startD = start[3];
        endA = end[0]; endB = end[1]; endC = end[2]; endD = end[3];
        this.startIP = toIP(startA, startB, startC, startD);
        this.endIP = toIP(endA, endB, endC, endD);
    }

    private int[] processIP(String IP) { // splits IP string by octet and stores in int[]
        int[] ans = new int[4];
        String[] splits = IP.split("\\.");
        ans[0] = Integer.parseInt(splits[0]);
        ans[1] = Integer.parseInt(splits[1]);
        ans[2] = Integer.parseInt(splits[2]);
        ans[3] = Integer.parseInt(splits[3]);
        return ans;
    }

    // Provides long representation by constructing from each octet
    // Used for comparison as toIP(ip1) > toIP(ip2) if ip1 > ip2
    private static Long toIP(int A, int B, int C, int D) {
        return ((long) A << 24) + ((long) B << 16) + ((long) C << 8) + ((long) D);
    }

    @Override
    public int compareTo(OctetRange o) {
        return Long.compare(this.startIP, o.startIP); // only compares start IPs
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OctetRange that = (OctetRange) o;
        return startA == that.startA &&
                startB == that.startB &&
                startC == that.startC &&
                startD == that.startD;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startA, startB, startC, startD);
    }
}
