package sg.edu.nus.iss.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.backend.models.Archive;
import sg.edu.nus.iss.backend.models.BundleInfo;

@Repository
public class ArchiveRepository {
    
    @Autowired
	MongoTemplate mongoTemp;

    public void recordBundle(Archive archive){
        mongoTemp.save(archive, "archives");
    }

    public Optional<Archive> getBundleByBundleId(String id){
        Archive archive = mongoTemp.findById(id, Archive.class, "archives");
        
        return Optional.ofNullable(archive);
    }
    
    /*
        db.archives.aggregate([
        {
            $project: {
                title : 1,
                date : 1
                }   
        },
        {
            $sort:{
                date : -1,
                title : 1
                }
        }   
        
    ])
     */
    public List<BundleInfo> getBundles(){
        ProjectionOperation projOpt = Aggregation.project()
                                        .andInclude("title", "date");
        SortOperation sortOpt = Aggregation.sort(Direction.DESC, "date").and(Direction.ASC, "title");
        Aggregation pipeline = Aggregation.newAggregation(projOpt, sortOpt);

        List<BundleInfo> archives = mongoTemp.aggregate(pipeline, "archives", BundleInfo.class).getMappedResults();
        
        return archives;
    }   
}
