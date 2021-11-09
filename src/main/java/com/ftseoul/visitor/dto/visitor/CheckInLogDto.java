package com.ftseoul.visitor.dto.visitor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ftseoul.visitor.dto.visitor.projection.CheckInVisitor;
import com.ftseoul.visitor.dto.visitor.projection.CheckInVisitorDecrypt;
import com.ftseoul.visitor.encrypt.Seed;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckInLogDto implements Serializable {
    private List<CheckInVisitorDecrypt> checkInLogs;
    private int lastPage;

    @JsonIgnore
    private boolean decrypted = false;

    public CheckInLogDto(List<CheckInVisitorDecrypt> checkInLogs, int lastPage) {
        this.checkInLogs = checkInLogs;
        this.lastPage = lastPage;
    }

    public CheckInLogDto decrypt(Seed seed) {
        if (!decrypted) {
            this.checkInLogs = checkInLogs
                .stream()
                .map(checkInVisitor ->
                    CheckInVisitorDecrypt.builder()
                        .checkInDate(checkInVisitor.getCheckInDate())
                        .checkIn(checkInVisitor.getCheckIn())
                        .id(checkInVisitor.getId())
                        .name(seed.decrypt(checkInVisitor.getName()))
                        .phone(seed.decrypt(checkInVisitor.getPhone()))
                        .organization(checkInVisitor.getOrganization())
                        .status(checkInVisitor.getStatus())
                        .staffName(seed.decrypt(checkInVisitor.getStaffName()))
                        .staffPhone(seed.decrypt(checkInVisitor.getStaffPhone()))
                        .staffDepartment(checkInVisitor.getStaffDepartment())
                        .purpose(checkInVisitor.getPurpose())
                        .place(checkInVisitor.getPlace())
                        .build())
                .collect(Collectors.toList());
            decrypted = true;
        }
        return this;
    }
}
