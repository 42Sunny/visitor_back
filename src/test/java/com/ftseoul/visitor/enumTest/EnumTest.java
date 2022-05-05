package com.ftseoul.visitor.enumTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.policy.ReserveType;
import org.junit.jupiter.api.Test;

import java.util.List;

public class EnumTest {


    public static class EnumTestDto{
        private String place;
        private String targetStaffName;
        private String purpose;
        private ReserveType type;
        private List<Visitor> visitor;

        public EnumTestDto(){};

        public EnumTestDto(String place, String targetStaffName, String purpose, ReserveType type, List<Visitor> visitor) {
            this.place = place;
            this.targetStaffName = targetStaffName;
            this.purpose = purpose;
            this.type = type;
            this.visitor = visitor;
        }

        public String getPlace() {
            return place;
        }

        public String getTargetStaffName() {
            return targetStaffName;
        }

        public String getPurpose() {
            return purpose;
        }

        public ReserveType getType() {
            return type;
        }

        public List<Visitor> getVisitor() {
            return visitor;
        }

        @Override
        public String toString() {
            return "EnumTestDto{" +
                    "place='" + place + '\'' +
                    ", targetStaffName='" + targetStaffName + '\'' +
                    ", purpose='" + purpose + '\'' +
                    ", type=" + type +
                    ", visitor=" + visitor +
                    '}';
        }
    }
    @Test
    void jsonToEnum() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String str = "{\"place\":1, \"targetStaffName\":\"kim\", \"purpose\":\"mentoring\", \"type\":\"DEFAULT\", " +
                "\"visitor\":[]}";
        EnumTestDto reserveVisitorDto = objectMapper.readValue(str, EnumTestDto.class);
        System.out.println(reserveVisitorDto);
    }

}
