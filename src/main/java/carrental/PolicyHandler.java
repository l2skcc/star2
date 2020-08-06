package carrental;

import carrental.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{

    @Autowired StarRepository starRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCarRented_StarInsert(@Payload CarRented carRented){
        System.out.println("##### listener StarInsert : " + carRented.toJson());
        if(carRented.isMe()){
            Star star = new Star();
            star.setId(carRented.getId());
            if ("CAR_RENTAL_CANCELED".equals(carRented.getProcStatus())) {
                star.setStars("x");
            } else if ("PAID".equals(carRented.getProcStatus())) {
                star.setStars("w");
            }
            star.setStars("-");
            star.setResrvNo(carRented.getResrvNo());
            star.setCarNo(carRented.getCarNo());
            starRepository.save(star);
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCarRentalCanceled_StarInsert(@Payload CarRentalCanceled carRentalCanceled){
        if(carRentalCanceled.isMe()){
            System.out.println("##### listener StarInsert : " + carRentalCanceled.toJson());
            Star star = new Star();
            star.setId(carRentalCanceled.getId());
            if (starRepository.findByResrvNo(carRentalCanceled.getResrvNo()).size() > 0){
                star = starRepository.findByResrvNo(carRentalCanceled.getResrvNo()).get(0);
            }
            star.setStars("x");
            star.setResrvNo(carRentalCanceled.getResrvNo());
            star.setCarNo(carRentalCanceled.getCarNo());
            starRepository.save(star);
        }
    }


}
