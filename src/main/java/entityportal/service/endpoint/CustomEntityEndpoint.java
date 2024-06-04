package entityportal.service.endpoint;

import entityportal.service.model.Entity;
import entityportal.service.service.CustomEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/custom")
public class CustomEntityEndpoint {
    private final CustomEntityService customEntityService;

    @Autowired
    public CustomEntityEndpoint(CustomEntityService customEntityService) {
        this.customEntityService = customEntityService;
    }

    @GetMapping()
    public ResponseEntity<List<Entity>> getAllEntities(){
        List<Entity> entityList = customEntityService.getEntities();
        if(entityList !=null&&!entityList.isEmpty()){
            return ResponseEntity.ok(entityList);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Entity> modifyEntity(@PathVariable("id") Long id,
                                                    @RequestBody Entity request){
        request.setId(id);
        Entity modified= customEntityService.modify(request);
        if(modified!=null){
            return ResponseEntity.ok(modified);
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entity> getEntityByID(@PathVariable("id") Long id
                                                        ){

        Entity response= customEntityService.getEntityByID(id);
        if(response!=null){
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.unprocessableEntity().build();
    }
}
