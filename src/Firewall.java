import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import org.junit.Assert;

public class Firewall {

    final PortIPRules inTCP = new PortIPRules();
    final PortIPRules inUDP = new PortIPRules();
    final PortIPRules outTCP = new PortIPRules();
    final PortIPRules outUDP = new PortIPRules();

    // selection[x][y]: x - direction, y - protocol
    // x = 0 - inbound, 1 - outbound
    // y = 0 - TCP, 1 - UDP
    final PortIPRules[][] selection = new PortIPRules[2][2];

    public Firewall(String filename) {
        selection[0][0] = inTCP; selection[0][1] = inUDP;
        selection[1][0] = outTCP; selection[1][1] = outUDP;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] rule = line.split(",");

                int direction = rule[0].equals("inbound")? 0 : 1;
                int protocol = rule[1].equals("tcp")? 0 : 1;
                PortIPRules ruleSet = selection[direction][protocol];
                ruleSet.addPortRule(rule[2]);
                ruleSet.addIPRule(rule[3]);
            }
        } catch (FileNotFoundException fe) {
            System.out.println(filename + "not found");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean accept_packet(String direction, String protocol, int port, String ip_address) {
        int dir = direction.equals("inbound")? 0 : 1;
        int pro = protocol.equals("tcp")? 0 : 1;
        PortIPRules ruleSet = selection[dir][pro];
        return ruleSet.match(String.valueOf(port), ip_address);
    }
}
