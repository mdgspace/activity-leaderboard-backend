package com.mdgspace.activityleaderboard.repository.redis;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.query.RedisQueryCreator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.redis.OrgStats;
import com.mdgspace.activityleaderboard.services.reddis.RedisConfiguration;


@Repository
public class OrgStatsRepository {

   @Autowired
   private RedisTemplate redisTemplate;
   
   private static final String HASH_KEY="OrgStats";
   // private RedisTemplate<String, OrgStats> redisTemplate=;

   public void save(OrgStats orgStats) {
      redisTemplate.opsForHash().put(HASH_KEY, orgStats.getId(), orgStats);
   }

   public List<OrgStats> findAll(){
      return redisTemplate.opsForHash().values(HASH_KEY);
   }
   public OrgStats findById(int id) {
      return (OrgStats)redisTemplate.opsForHash().get(HASH_KEY,id);
   }
   
   public void deleteById(int id){
      redisTemplate.opsForHash().delete(HASH_KEY, id);
   }
  


   // @RedisQueryCreator("#{#entityName}:organization:#{#organization} AND
   // #{#entityName}:monthly:#{#monthly}")
   // Optional<OrgStats> findByOrganizationAndMonthly(Organization organization,
   // Boolean monthly);

}
