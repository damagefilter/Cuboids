package net.playblack.cuboids.help;

import java.util.ArrayList;

public class HelpContext {
    private ArrayList<String> taxonomy;
    private String toolTip;

    public HelpContext(String toolTip, String[] taxonomy) {
        this.taxonomy = new ArrayList<String>(taxonomy.length);
        this.toolTip = toolTip;
        for (String c : taxonomy) {
            this.taxonomy.add(c.toLowerCase());
        }
    }

    /**
     * Search in the taxonomy terms of this HelpContext for a keyword to fit.
     * 
     * @param terms
     * @return True if keyword was found, false otherwise
     */
    public boolean searchFor(String terms) {
        if (taxonomy.isEmpty()) {
            return true;
        }
        return taxonomy.contains(terms);
    }

    public boolean searchFor(String[] needles) {
        if (taxonomy.isEmpty() || needles.length == 0) {
            return true;
        }
        for (String term : needles) {
            if (taxonomy.contains(term)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the toolTip for this HelpContext
     * 
     * @return
     */
    public String getToolTip() {
        return toolTip;
    }
}
