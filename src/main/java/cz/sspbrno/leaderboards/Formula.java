package cz.sspbrno.leaderboards;

public class Formula {
    public static double formula(int listSize, String placement, String percent, String device) {
        if (placement == null) return 0;
        if (percent == null) return 0;
        String[] perc = percent.split("-");
        double number = (listSize / (listSize / 5 + (-listSize / 5 + 1) * Math.pow(Math.E, -0.009 * Integer.parseInt(placement))));
        final double dev = Math.log10(resolve(device)) / 10;
        switch (perc.length) {
            case 1:
                if (Integer.parseInt(percent) == 100) number = number / dev;
                else  number = number * (Integer.parseInt(perc[0]) * 0.001) / dev;//number *= (((Integer.parseInt(perc[0]) * 0.005)) / dev);
                break;
            case 2:
                number *= ((((Integer.parseInt(perc[1]) - Integer.parseInt(perc[0])) * 0.005)) / dev);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + perc.length);
        }
        return number;
    }

    private static int resolve(String device) {
        if ((device.toLowerCase()).equals("mobile")) return (int) (5950000*0.5);
        else if (device.contains("fps"))
            return (int) (Integer.parseInt(device.replace("fps", "00000")) +
                    Integer.parseInt(device.replace("fps", "00000")) * 0.515);
        else return (int) (Integer.parseInt(device.replace("hz", "00000")) * 0.5);
    }
}
