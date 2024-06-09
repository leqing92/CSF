package sg.edu.nus.iss.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import sg.edu.nus.iss.backend.repository.ArchiveRepository;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner{

	@Autowired
	ArchiveRepository archiveRepo;
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		// List<BundleInfo> list = archiveRepo.getBundles();

		// for(BundleInfo a : list){
		// 	System.out.println(a.getBundleId());
		// }
	}

}
