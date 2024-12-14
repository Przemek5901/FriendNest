package pl.polsl.friendnest.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.polsl.friendnest.model.Hashtag;
import pl.polsl.friendnest.model.HashtagTo;
import pl.polsl.friendnest.model.PostHashtag;
import pl.polsl.friendnest.repository.PostHashtagRepository;
import pl.polsl.friendnest.service.HashtagService;

import java.util.ArrayList;
import java.util.List;


@Service
public class HashtagServiceImpl implements HashtagService {

    private final PostHashtagRepository postHashtagRepository;

    public HashtagServiceImpl(PostHashtagRepository postHashtagRepository) {
        this.postHashtagRepository = postHashtagRepository;
    }

    @Override
    public List<HashtagTo> getHashtagToList() {
        List<Hashtag> hashtagList = postHashtagRepository.findTopHashtagsByPostCount(PageRequest.of(0, 5));

        List<HashtagTo> hashtagToList = new ArrayList<>();

        if(hashtagList != null && !hashtagList.isEmpty()){
            for(Hashtag hashtag: hashtagList) {
                var postHashtagTo = HashtagTo.builder()
                        .hashtag(hashtag)
                        .hashtagCount(postHashtagRepository.countByHashtag(hashtag))
                        .build();

                hashtagToList.add(postHashtagTo);
            }
        }
        return hashtagToList;
    }
}
