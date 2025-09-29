class Time {
    private int hours;
    private int minutes;

    public Time(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int toMinutes() {
        return hours * 60 + minutes;
    }

    public String toString() {
        return hours + "h " + minutes + "min";
    }
}

class Position {
    private double x;
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

class AGV {
    private String ID;
    private double batteryLoad;
    private double consumption;
    private Time chargingTime;
    private Position position;
    private float maxSpeed;
    private float actSpeed;

    public void setData(String ID, double batteryLoad, double consumption,
                        Time chargingTime, Position position,
                        float maxSpeed, float actSpeed) {
        this.ID = ID;
        this.batteryLoad = batteryLoad;
        this.consumption = consumption;
        this.chargingTime = chargingTime;
        this.position = position;
        this.maxSpeed = maxSpeed;
        this.actSpeed = actSpeed;
    }

    public String getData() {
        return ID + " | Battery: " + batteryLoad + "%" +
                " | Cons.: " + consumption + "kWh" +
                " | Charge Time: " + chargingTime +
                " | Position: " + position +
                " | Max Speed: " + maxSpeed + " m/s" +
                " | Actual Speed: " + actSpeed + " m/s";
    }

    public double getConsumption() {
        return consumption;
    }
}

class IOperation {
    private String ID;
    private String description;
    private Time nominalTime;
    private AGV[] resources;

    public void setData(String ID, String description, Time nominalTime, AGV[] resources) {
        this.ID = ID;
        this.description = description;
        this.nominalTime = nominalTime;
        this.resources = resources;
    }

    public int getDuration() {
        return nominalTime.toMinutes();
    }

    public double getEnergy() {
        double total = 0;
        for (AGV agv : resources) {
            total += agv.getConsumption();
        }
        return total;
    }

    public String getData() {
        String info =   "Operation: " + ID + ", " + description + ", Time: " + nominalTime + "\n" +
                        "AGVs involved:\n";
        for (AGV agv : resources) {
            info += "   " + agv.getData() + "\n";
        }
        return info;
    }

    public int getAGVCount() {
        return resources.length;
    }
}

class IndustrialProcess {
    private String ID;
    private IOperation[] operations;

    public void setData(String ID, IOperation[] operations) {
        this.ID = ID;
        this.operations = operations;
    }

    public int processDuration() {
        int total = 0;
        for (IOperation op : operations) {
            total += op.getDuration();
        }
        return total;
    }

    public double processEnergyConsumption() {
        double total = 0;
        for (IOperation op : operations) {
            total += op.getEnergy();
        }
        return total;
    }

    public int processAGVCount() {
        int count = 0;
        for (IOperation op : operations) {
            count += op.getAGVCount();
        }
        return count;
    }

    public void printInfo() {
        System.out.println("Industrial Process: " + ID);
        System.out.println("Total Time: " + processDuration() + " minutes");
        System.out.println("Total Energy: " + processEnergyConsumption() + " kWh");
        System.out.println("Total AGVs used: " + processAGVCount());
        System.out.println("Operations Info:");
        for (IOperation op : operations) {
            System.out.println(op.getData());
        }
    }
}

class Warehouse {
    private String ID;
    private IndustrialProcess[] processes;

    public Warehouse(String ID, IndustrialProcess[] processes) {
        this.ID = ID;
        this.processes = processes;
    }

    public int totalWarehouseTime() {
        int total = 0;
        for (IndustrialProcess p : processes) {
            total += p.processDuration();
        }
        return total;
    }

    public double totalWarehouseEnergy() {
        double total = 0;
        for (IndustrialProcess p : processes) {
            total += p.processEnergyConsumption();
        }
        return total;
    }

    public void printWarehouseInfo() {
        System.out.println("=-=-=-=-=-= Warehouse: " + ID + " =-=-=-=-=-=");
        int totalAGVs = 0;
        for (IndustrialProcess p : processes) {
            p.printInfo();
            totalAGVs += p.processAGVCount(); // sum total AGVs across processes
            System.out.println("-------------------");
        }
        System.out.println("TOTAL for Warehouse:");
        System.out.println("Total Time: " + totalWarehouseTime() + " minutes");
        System.out.println("Total Energy: " + totalWarehouseEnergy() + " kWh");
        System.out.println("Total AGVs used across all processes: " + totalAGVs);
    }

}

public class Main {
    public static void main(String[] args) {
        AGV agv1 = new AGV();
        agv1.setData("AGV1", 80, 5.5, new Time(1, 30), new Position(0, 0), 2.0f, 1.5f);
        AGV agv2 = new AGV();
        agv2.setData("AGV2", 90, 6.0, new Time(1, 0), new Position(1, 2), 2.5f, 2.0f);
        AGV agv3 = new AGV();
        agv3.setData("AGV3", 85, 5.0, new Time(1, 15), new Position(2, 1), 2.2f, 1.8f);

        AGV[] op1AGVs = {agv1, agv2};
        IOperation op1 = new IOperation();
        op1.setData("OP1", "Move pallets", new Time(2, 0), op1AGVs);
        AGV[] op2AGVs = {agv2, agv3};
        IOperation op2 = new IOperation();
        op2.setData("OP2", "Load shelves", new Time(1, 30), op2AGVs);
        AGV[] op3AGVs = {agv1, agv3};
        IOperation op3 = new IOperation();
        op3.setData("OP3", "Transport goods", new Time(2, 15), op3AGVs);

        IOperation[] process1Ops = {op1, op2};
        IndustrialProcess process1 = new IndustrialProcess();
        process1.setData("Process 01", process1Ops);
        IOperation[] process2Ops = {op2, op3};
        IndustrialProcess process2 = new IndustrialProcess();
        process2.setData("Process 02", process2Ops);

        IndustrialProcess[] processes = {process1, process2};
        Warehouse warehouse = new Warehouse("Main Warehouse of Group14", processes);

        warehouse.printWarehouseInfo();
    }
}
