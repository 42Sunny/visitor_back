package com.ftseoul.visitor.policy;


import com.ftseoul.visitor.data.Reserve;
import com.ftseoul.visitor.dto.reserve.ReserveVisitorDto;

/**
 * reserveController factory policy
 */
public interface ReservePolicy {

    ReserveType getType();
    Reserve saveReserve(ReserveVisitorDto reserveVisitorDto);
}
