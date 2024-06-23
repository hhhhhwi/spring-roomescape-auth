package roomescape.reservation.service;

import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.stereotype.Service;
import roomescape.error.exception.MemberNotExistsException;
import roomescape.error.exception.PastDateTimeException;
import roomescape.error.exception.ReservationTimeNotExistsException;
import roomescape.error.exception.ThemeNotExistsException;
import roomescape.member.Member;
import roomescape.member.service.MemberRepository;
import roomescape.reservation.Reservation;
import roomescape.reservation.dto.AdminReservationRequest;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservationTime.ReservationTime;
import roomescape.reservationTime.service.ReservationTimeRepository;
import roomescape.theme.Theme;
import roomescape.theme.service.ThemeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository,
        ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository,
        MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public List<ReservationResponse> findReservations() {
        return reservationRepository.find().stream()
            .map(ReservationResponse::new)
            .collect(Collectors.toList());
    }

    public ReservationResponse saveReservationByAdmin(String name, ReservationRequest request) {
        ReservationTime reservationTime = Optional.ofNullable(
                reservationTimeRepository.findById(request.getTimeId()))
                .orElseThrow(ReservationTimeNotExistsException::new);
        Theme theme = Optional.ofNullable(themeRepository.findById(request.getThemeId()))
                                .orElseThrow(ThemeNotExistsException::new);

        Reservation reservation = new Reservation(name, request.getDate(),
            reservationTime, theme);

        if (reservation.isBeforeThanNow()) {
            throw new PastDateTimeException();
        }

        if (reservationRepository.countByDateAndTimeAndTheme(reservation.getDate(),
            reservationTime.getId(), theme.getId()) > 0) {
            throw new DuplicateRequestException("해당 시간 예약이");
        }

        return new ReservationResponse(reservationRepository.save(reservation));
    }

    public ReservationResponse saveReservationByAdmin(AdminReservationRequest request) {
        Member member = Optional.ofNullable(memberRepository.findById(request.getMemberId()))
            .orElseThrow(MemberNotExistsException::new);
        return saveReservationByAdmin(member.getName(), request);
    }

    public void deleteReservation(long id) {
        reservationRepository.delete(id);
    }
}
