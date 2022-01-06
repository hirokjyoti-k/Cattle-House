package in.adbu.cattlehouse;

public class CattleData {

    public double Latitude;
    public double Longitude;
    public int HeartRate;
    public int Temperature;
    public int Weight;
    public int Age;
    public String Movement;
    public String Type;
    public String Gender;
    public String Uid;
    public String Beeding;
    public String LastVaccine;


    public CattleData() {
    }

    public CattleData(double Latitude, double Longitude, int HeartRate, int Temperature, int Weight, int Age, String Movement, String Type, String Gender, String Uid, String Beeding, String LastVaccine) {
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.HeartRate = HeartRate;
        this.Temperature = Temperature;
        this.Weight = Weight;
        this.Age = Age;
        this.Movement = Movement;
        this.Type = Type;
        this.Gender = Gender;
        this.Uid = Uid;
        this.Beeding = Beeding;
        this.LastVaccine = LastVaccine;
    }

}
