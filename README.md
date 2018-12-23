# Illumio PCE Assignment 2018

## Testing
All tests are located in `test/FirewallTest.java`. I used JUnit for testing and each test includes comments about what edge case it tries to cover, in addition to the example tests.

## Implementation Details
The implementation follows an object-oriented approach with the following classes:

* **`Firewall.java`**: main firewall class that has four objects of `PortIPRules`, each corresponding to one of the combinations of direction (inbound/outbound) and protocol (tcp/udp).
* **`PortIPRules.java`**: contains HashSets of individual IPs/ports and TreeSets for IP/port ranges. Provides methods `addIPRule` and `addPortRule` for adding IP/port rules and `match` for checking whether the input matches any rule utilizing methods `matchPort` and `matchIP`.
* **`OctetRange.java`**: represents an IP range. Objects are compared only using the start of the range. Uses method `toIP` to convert IP address to `Long` representation in order to help in comparison.
* **`PortRange.java`**: represents a port range. Objects are compared only using the start of the range.

For checking whether an input packet matches any rule, the following checks take place:
* Check if the input IP/port exists in the individual port/IP HashSets.
* If the above check fails, check if the input IP exists within any of the ranges in the IP ranges' TreeSet. The TreeSet helps in maintaining the IP ranges in sorted order in order to allow for efficient lookups. The [TreeSet.lower](https://docs.oracle.com/javase/7/docs/api/java/util/TreeSet.html#lower(E)) method returns the `OctetRange` object with the greatest start of the range that is strictly less than the input IP. This object's end of the range is then compared with the input IP to see if it falls within that range.
* A similar check is done for checking whether the input port falls within any of the ranges in the port ranges' TreeSet.

**Assumption:** Implementation assumes that the IP/port ranges in the input rules do not overlap with each other. Since only the start of the range is used for maintaining sorted order, the implementation could have false negatives in certain cases if this assumption is violated.

## Performance Analysis
For each (direction, protocol), the following applies to each of the IP and port lookups.<br>
Let the number of rules be `N`, among which `R` contain ranges such that the total number of affected IPs/ports is `P`.

Lookup in the `HashSet` is O(1) amortized. Storing IPs as Strings in the HashSet helps in utilizing Java's hashCode for Strings and leads to good spread => good performance guarantees. <br>Lookup in the `TreeSet` is [O(log R)](https://docs.oracle.com/javase/7/docs/api/java/util/TreeSet.html).

* **Time Complexity:** Best - `O(1)`, Worst - `O(log R)`

Storing all possible allowed IPs/ports in a HashSet might potentially allow for O(1) lookups even in the worst case but it would lead to a blow-up in space complexity of O(P), which could be a problem for large R (which is often the case in firewalls) as then, P >> N. Using a TreeSet to store only the ranges does not lead to this problem.
* **Space Complexity:** `O(N)`


(These are theoretical guarantees; I was unable to perform any performance testing within the time limit to verify them)

## Possible Improvements
* Using a LPM (Longest Prefix Match) tree to match IP addresses might reduce the time complexity for the range IP lookup
* Some of the methods seem to do the same thing so the idea would be to implement abstract methods and cut down repetitive code
* Extensive testing to verify performance guarantees
* Multi-threaded check for port/IP
* Input validation

## Team
I would be happy to be a part of the ***Platform*** team.
