package domain.entities.hogar;

import java.util.List;

public class HogarDTO {
    public int total;
    public int offset;
    public List<Hogar> hogares;
    public String error;

    public int getTotal() {
        return total;
    }

    public int getOffset() {
        return offset;
    }

    public List<Hogar> getHogares() {
        return hogares;
    }

    public String getError() {
        return error;
    }
}
