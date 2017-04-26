<?php
/**
 * Grab Snaffles and try to throw them through the opponent's goal!
 * Move towards a Snaffle and use your team id to determine where you need to throw it.
 **/

// Ranked at 831/2399

fscanf(STDIN, "%d",
    $myTeamId // if 0 you need to score on the right of the map, if 1 you need to score on the left
);

$arrayTargets = array();

/*class Sort {
    private $id = "";
    private $price = 0;
    private $time = 0;
    private $targetId = 0;
    
    public function getId(){
        return $id;   
    }    
    public function getPrice(){
        return $price;   
    }    
    public function getTime(){
        return $time;   
    }    
    public function getTargetid(){
        return $targetId;   
    }
    
    public function setId($id){
        $this->id = $id;
    }
    public function setPrice($price){
        $this->price =  $price;   
    }    
    public function setTime($time){
        $this->time =  $time;   
    }    
    public function setTargetid($targetId){
        $this->targetId =  $targetId;   
    }  
}
$arraySorts = array();
$sort = new Sort();
$sort->setId("OBLIVIATE");
$sort->setPrice(5);
$sort->setTime(3);
$arraySorts[0] = $sort;
$sort->setId("PETRIFICUS");
$sort->setPrice(10);
$sort->setTime(1);
$arraySorts[1] = $sort;
$sort->setId("ACCIO");
$sort->setPrice(20);
$sort->setTime(6);
$arraySorts[2] = $sort;
$sort->setId("FLIPENDO");
$sort->setPrice(20);
$sort->setTime(3);
$arraySorts[3] = $sort;*/


$target0 = new StdClass();
$target0->x = 16001;
$target0->y = 3750;
$arrayTargets[] = $target0;

$target1 = new StdClass();
$target1->x = -1;
$target1->y = 3750;
$arrayTargets[] = $target1;

$k = 0;

$shootWizard0 = 0;
$shootedBalls0 = 0;

$shootWizard1 = 0;
$shootedBalls1 = 0;

$arraySnaffles;


// game loop
while (TRUE)
{
    
    $arrayWizards = array();
    $arraySnaffles = array();
    $arrayOpenents = array();
    $arrayBludgers = array();
    
    // pour éviter de retarget
    $secondArray = array();
    
    fscanf(STDIN, "%d",
        $entities // number of entities still in game
    );
    for ($i = 0; $i < $entities; $i++)
    {
        fscanf(STDIN, "%d %s %d %d %d %d %d",
            $entityId, // entity identifier
            $entityType, // "WIZARD", "OPPONENT_WIZARD" or "SNAFFLE" (or "BLUDGER" after first league)
            $x, // position
            $y, // position
            $vx, // velocity
            $vy, // velocity
            $state // 1 if the wizard is holding a Snaffle, 0 otherwise
        );
        
        $entity = new StdClass();
        $entity->entityId = $entityId;
        $entity->entityType = $entityType;
        $entity->x = $x;
        $entity->y = $y;
        $entity->vx = $vx;
        $entity->vy = $vy;
        $entity->state = $state;
        
        // Classes entities
        if($entityType == "SNAFFLE"){
            $entity->target = 0;
            $arraySnaffles[] = $entity;
        }else 
        if($entityType == "WIZARD"){
            $arrayWizards[] = $entity;
        }else 
        if($entityType == "OPPONENT_WIZARD"){
            $arrayOpenents[] = $entity;
        }else 
        if($entityType == "BLUDGER"){
            $arrayBludgers[] = $entity;
        }
        //error_log(var_export("entityId   : ".$entityId, true));
    }
    
    // Mes 2 sorciers
    for ($i = 0; $i < 2; $i++)
    {
        
        // test speed
        $speed = 140;
        if($myTeamId == 0){
            if($arrayWizards[0]->x<11000){
                $speed = $speed + 10;
            }
        }else{
            if($arrayWizards[0]->x>5000){
                $speed = $speed + 10;
            }
        }
        
        
        if($i==0){
            
            if($arrayWizards[$i]->state == 1){
                
                $S = findFirstSnaffle($arrayWizards[0], $arraySnaffles);
                $shootedBalls0 = $S;
                $shootWizard0++ ;

                // LAUNCH
                echo ("THROW ". $arrayTargets[$myTeamId]->x." ". $arrayTargets[$myTeamId]->y ." 500\n");
            }else{
                
                if($shootWizard0>0 && $k/20>=1 && isset($arraySnaffles[$shootedBalls0]) && testSnaffleBetweenMeAndGoal($arrayWizards[$i], $arraySnaffles[$shootedBalls0], $arrayTargets,$myTeamId)){
                    echo ("FLIPENDO ". $arraySnaffles[$shootedBalls0]->entityId ."\n");
                    $shootWizard0 = 0;
                    $shootedBalls0 = 0;
                    $k = $k-20;
                }else if($k/20>=1 && count($arraySnaffles)<=2){
                    echo ("ACCIO ". $arraySnaffles[0]->entityId ."\n");
                    $k = $k-20;
                }else {
                    
                    // CHOICE CLOSEST
                    $snaffleKey = findFirstSnaffle($arrayWizards[0], $arraySnaffles);
                    $arraySnaffles[$snaffleKey]->target=1;
                    $snaffle = $arraySnaffles[$snaffleKey];
                    
                    
                    echo ("MOVE ". $snaffle->x ." ". ($snaffle->y) ." ".$speed."\n");
                }
            }
        }
        else 
        if($i==1){
            foreach($arraySnaffles as $newsnaffle){
                if($newsnaffle->target == 0){
                    $secondArray[] = $newsnaffle;
                }
            }
            
            if($arrayWizards[$i]->state == 1){
                
                $S = findFirstSnaffle($arrayWizards[0], $secondArray);
                $shootedBalls1 = $S;
                $shootWizard1++ ;
                // LAUNCH
                echo ("THROW ". $arrayTargets[$myTeamId]->x ." ". $arrayTargets[$myTeamId]->y ." 500\n");
            
            }else{
                
                if($shootWizard1>0 && $k/20>=1 && isset($secondArray[$shootedBalls1]) && testSnaffleBetweenMeAndGoal($arrayWizards[$i], $secondArray[$shootedBalls1], $arrayTargets,$myTeamId)){
                    echo ("FLIPENDO ". $secondArray[$shootedBalls1]->entityId ."\n");
                    $shootWizard1 = 0;
                    $shootedBalls1 = 0;
                    $k = $k-20;
                }/*else if($k/10>=1 && count($secondArray)<=2 && isset($secondArray[0])){
                    echo ("PETRIFICUS ". $secondArray[0]->entityId ."\n");
                    $k = $k-10;
                }*/else {
                    // CHOICE CLOSEST
                    $snaffleKey2 = findFirstSnaffle($arrayWizards[1], $secondArray);
                    $snaffle2 = isset($secondArray[$snaffleKey2]) ? $secondArray[$snaffleKey2] : $arraySnaffles[0];
                    echo ("MOVE ". $snaffle2->x ." ". ($snaffle2->y) ." ".$speed."\n");
                }
            }
        }
    }
    
    foreach($arraySnaffles as $snaffle){
        $snaffle->target = 0;
    }
    $k++;
}

function findFirstSnaffle($wizardEntity, $arraySnaffles){    
    // Grosse valeur
    $tempObjIndexSnaffle = null;
    $tempValSnaffle = 999999999;
    $minValSnaffle = 999999999;
    
    $toSortArray = array();
    
    //Pour chaque SNAFFLE
    for($i=0; $i<count($arraySnaffles); $i++){
        $snaffle = $arraySnaffles[$i];

        $diffX = ( $snaffle->x) - ($wizardEntity->x) ;
        $diffY = ( $snaffle->y) - ($wizardEntity->y) ;
        
        // distance euclidienne
        $tempValSnaffle = sqrt(( $diffX * $diffX )  +  ( $diffY * $diffY ));
        if($minValSnaffle > $tempValSnaffle){
            $minValSnaffle = $tempValSnaffle;
            $tempObjIndexSnaffle = $i;
        }
        
        // TODO vecteur vitesse / angle / collision
    }
    
    // Je tri et garde le SNAFFLE le plus proche
    //ksort($toSortArray);
    //$key = key($toSortArray);
    error_log(var_export("key   : ".$tempObjIndexSnaffle, true));
    return (isset($tempObjIndexSnaffle)) ? ($tempObjIndexSnaffle) : 0;
}

function testSnaffleBetweenMeAndGoal($wizard, $snaffle, $arrayTargets, $myTeamId){
    if((($wizard->x > $snaffle->x && $snaffle->x > $arrayTargets[$myTeamId]->x)
      || ($wizard->x < $snaffle->x && $snaffle->x < $arrayTargets[$myTeamId]->x)) &&
      (($wizard->y > $snaffle->y && $snaffle->y > $arrayTargets[$myTeamId]->y)
        || ($wizard->y < $snaffle->y && $snaffle->y < $arrayTargets[$myTeamId]->y))){
        return true;
        }else{
        return false;    
        }
}


?>
