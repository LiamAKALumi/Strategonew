package com.example.stratego;
//
import java.util.*;

public class AIPlayer {
    private GameModel model;
    private Random randomFactor;
    private Piece flag;
    private Piece suspectedEnemyFlag;
    private double score;


    Iterator it;





    public AIPlayer(GameModel model) {
        this.model = model;
        this.randomFactor = new Random();
        this.score=0;
        it = model.getFullBoard().getComputerPieces().iterator();
        for(Piece p:model.getFullBoard().getComputerPieces()){
            if(p.getType().equals("F")){
                this.flag=p;
            }
        }
    }


    public void makeMove() {
        Move move = generateMove();
        System.out.println("AI move: " + move.toString());
        model.applyMove(move);
    }

    /*

    private Move generateMove() {
        int sourceX, sourceY, destX, destY;
        Piece sourcePiece, destPiece;
        do {
            sourceX = random.nextInt(10);
            sourceY = random.nextInt(10);
            destX = random.nextInt(10);
            destY = random.nextInt(10);
            sourcePiece = model.getBoard()[sourceX][sourceY];
            destPiece = model.getBoard()[destX][destY];
        } while (sourcePiece == null || !sourcePiece.getColor().equals("Red") ||
                (destPiece != null && !destPiece.getColor().equals("Blue")) ||
                !model.isMoveValid(new Move(sourceX, sourceY, destX, destY)));

        return new Move(sourceX, sourceY, destX, destY);
    }

     */

    private ArrayList<Piece> firstRowPieces()
    {
        ArrayList<Piece> arr = new ArrayList<>();
        for(int i=0;i<10;i++)
            if(model.getBoard()[3][i]!=null)
              arr.add(model.getBoard()[3][i]);

        return arr;
    }


    private Move openingStage(){
       double score;
       double maxScore=-999;
       Move m;
       Move maxMove=null;
       // go over every piece
       for(Piece p:model.getFullBoard().getComputerPieces()){
           // go over every movable piece
           if (model.isComputerMovablePiece(p.getPosX(),p.getPosY())) {
               m = new Move(p.getPosX(), p.getPosY(), p.getPosX() - 1, p.getPosY());
               if (p.getPosX() != 0 && model.isMoveValid(m)) {
                   score = 0;
                   if (!p.isRevealed()) {
                       score -= 0.2;
                   }
                   // if the move is an attack
                   if (model.getBoard()[m.getDestX()][m.getDestY()] != null && model.getBoard()[m.getDestX()][m.getDestY()].getColor().equals("Blue")) {
                       // if the opposing piece is known, calculate how worth it it is to attack that piece
                       if (model.getFullBoard().getKnownPieces().contains(model.getBoard()[m.getDestX()][m.getDestY()])) {
                           if (model.getInteractionResult(m)) {
                               score += model.getBoard()[m.getDestX()][m.getDestY()].getValue() * 3 - p.getValue();
                           } else {
                               score -= p.getValue() * 3;
                           }
                       }
                       // if the opposing piece is not known, first, increase the value of the score as you are revealing a piece. then, calculate how worth it is to attack the piece
                       else {
                           score += 10 - p.getValue() * 1.5;
                           for (Piece enemyPiece : model.getFullBoard().getPlayerPieces()) {
                               if (p.checkInteraction(enemyPiece)) {
                                   score += 0.1 * ((double) 40 / model.getFullBoard().getPlayerPieces().size());
                               } else {
                                   score -= p.getValue() * 0.1 * ((double) 40 / model.getFullBoard().getPlayerPieces().size());
                               }
                           }
                       }
                   }
                   // if the move isn't an attacking move
                   // discourage going backwards as it makes getting information take more time
                   // make it so that the less valuable pieces going backwards is more punishing
                   else {
                       score -= 1 + 0.1 * p.getValue();
                   }


                   // add a random factor between 0.9 and 1 in the end
                   score *= (double) (randomFactor.nextInt(11) + 90) / 100;
                   if (score > maxScore) {
                       maxScore = score;
                       maxMove = m;
                   }
               }
               //repeat for the other 3 directions

               //forwards

               m = new Move(p.getPosX(), p.getPosY(), p.getPosX() + 1, p.getPosY());
               if (p.getPosX() != 10 && model.isMoveValid(m)) {
                   score = 0;
                   if (!p.isRevealed()) {
                       score -= 0.2;
                   }
                   // if the move is an attack
                   if (model.getBoard()[m.getDestX()][m.getDestY()] != null && model.getBoard()[m.getDestX()][m.getDestY()].getColor().equals("Blue")) {
                       // if the opposing piece is known, calculate how worth it it is to attack that piece
                       if (model.getFullBoard().getKnownPieces().contains(model.getBoard()[m.getDestX()][m.getDestY()])) {
                           if (model.getInteractionResult(m)) {
                               score += model.getBoard()[m.getDestX()][m.getDestY()].getValue() * 3 - p.getValue();
                           } else {
                               score -= p.getValue() * 3;
                           }
                       }
                       // if the opposing piece is not known, first, increase the value of the score as you are revealing a piece. then, calculate how worth it is to attack the piece
                       else {
                           score += 10 - p.getValue() * 1.5;
                           for (Piece enemyPiece : model.getFullBoard().getPlayerPieces()) {
                               if (p.checkInteraction(enemyPiece)) {
                                   score += 0.1 * ((double) 40 / model.getFullBoard().getPlayerPieces().size());
                               } else {
                                   score -= p.getValue() * 0.1 * ((double) 40 / model.getFullBoard().getPlayerPieces().size());
                               }
                           }
                       }
                   }
                   // if the move isn't an attacking move
                   // encourage going forward as it makes getting information take less time
                   // make it so that the less valuable pieces going backwards is more punishing
                   else {
                       score += 0.1 + (0.2 - 0.02 * p.getValue());
                   }


                   // add a random factor between 0.9 and 1 in the end
                   score *= (double) (randomFactor.nextInt(11) + 90) / 100;
                   if (score > maxScore) {
                       maxScore = score;
                       maxMove = m;
                   }
               }

               // left

               m = new Move(p.getPosX(), p.getPosY(), p.getPosX(), p.getPosY() - 1);
               if (p.getPosY() != 0 && model.isMoveValid(m)) {
                   score = 0;
                   if (!p.isRevealed()) {
                       score -= 0.2;
                   }
                   // if the move is an attack
                   if (model.getBoard()[m.getDestX()][m.getDestY()] != null && model.getBoard()[m.getDestX()][m.getDestY()].getColor().equals("Blue")) {
                       // if the opposing piece is known, calculate how worth it it is to attack that piece
                       if (model.getFullBoard().getKnownPieces().contains(model.getBoard()[m.getDestX()][m.getDestY()])) {
                           if (model.getInteractionResult(m)) {
                               score += model.getBoard()[m.getDestX()][m.getDestY()].getValue() * 3 - p.getValue();
                           } else {
                               score -= p.getValue() * 3;
                           }
                       }
                       // if the opposing piece is not known, first, increase the value of the score as you are revealing a piece. then, calculate how worth it is to attack the piece
                       else {
                           score += 10 - p.getValue() * 1.5;
                           for (Piece enemyPiece : model.getFullBoard().getPlayerPieces()) {
                               if (p.checkInteraction(enemyPiece)) {
                                   score += 0.1 * ((double) 40 / model.getFullBoard().getPlayerPieces().size());
                               } else {
                                   score -= p.getValue() * 0.1 * ((double) 40 / model.getFullBoard().getPlayerPieces().size());
                               }
                           }
                       }
                   } else {
                       // if the move isn't an attacking move
                       //give a small benefit to going to the sides to make the option possible
                       int goToSides = randomFactor.nextInt(26);
                       score += goToSides * 0.01;

                       // add a random factor between 0.9 and 1 in the end
                       score *= (double) (randomFactor.nextInt(11) + 90) / 100;
                       if (score > maxScore) {
                           maxScore = score;
                           maxMove = m;
                       }
                   }
               }

               // right

               m = new Move(p.getPosX(), p.getPosY(), p.getPosX(), p.getPosY() + 1);
               if (p.getPosY() != 10 && model.isMoveValid(m)) {
                   score = 0;
                   if (!p.isRevealed()) {
                       score -= p.getValue() * 0.05;
                   }
                   // if the move is an attack
                   if (model.getBoard()[m.getDestX()][m.getDestY()] != null && model.getBoard()[m.getDestX()][m.getDestY()].getColor().equals("Blue")) {
                       // if the opposing piece is known, calculate how worth it it is to attack that piece
                       if (model.getFullBoard().getKnownPieces().contains(model.getBoard()[m.getDestX()][m.getDestY()])) {
                           if (model.getInteractionResult(m)) {
                               score += model.getBoard()[m.getDestX()][m.getDestY()].getValue() * 3 - p.getValue();
                           } else {
                               score -= p.getValue() * 3;
                           }
                       }
                       // if the opposing piece is not known, first, increase the value of the score as you are revealing a piece. then, calculate how worth it is to attack the piece
                       else {
                           score += 10 - p.getValue() * 1.5;
                           for (Piece enemyPiece : model.getFullBoard().getPlayerPieces()) {
                               if (p.checkInteraction(enemyPiece)) {
                                   score += 0.02 * ((double) 40 / model.getFullBoard().getPlayerPieces().size());
                               } else {
                                   score -= p.getValue() * 0.02 * ((double) 40 / model.getFullBoard().getPlayerPieces().size());
                               }
                           }
                       }
                   } else {
                       // if the move isn't an attacking move
                       //give a small benefit to going to the sides to make the option possible
                       int goToSides = randomFactor.nextInt(26);
                       score += goToSides * 0.01;
                       // add a random factor between 0.9 and 1 in the end
                       score *= (double) (randomFactor.nextInt(11) + 90) / 100;
                       if (score > maxScore) {
                           maxScore = score;
                           maxMove = m;
                       }
                   }
               }
           }
       }
       return maxMove;

        /*
        // check if the piece has moved yet
        // if not, give a small score reduction to moving it to discourage movement
        if(p.hasMoved()){
           score-=p.getRank()*0.1;
        }
        // check if the move is initiating an attack
        // if so, calculate how good it is to go for the attack
        if(model.getBoard()[move.getDestX()][move.getDestY()]!=null){

        }

         */




    }

    private Move generalMove() {
        double score;
        double maxScore = -999;
        Move m;
        Move maxMove = null;
        // go over every piece
        for (Piece p : model.getFullBoard().getComputerPieces()) {
            // go over every movable piece
            if (model.isComputerMovablePiece(p.getPosX(), p.getPosY())) {
                m = new Move(p.getPosX(), p.getPosY(), p.getPosX() - 1, p.getPosY());
                if (p.getPosX() != 0 && model.isMoveValid(m)) {
                    score = 0;
                    if (!p.isRevealed()) {
                        score -= 0.1;
                    }
                    // if the move is an attack
                    if (model.getBoard()[m.getDestX()][m.getDestY()] != null && model.getBoard()[m.getDestX()][m.getDestY()].getColor().equals("Blue")) {
                        // if the opposing piece is known, calculate how worth it it is to attack that piece
                        if (model.getFullBoard().getKnownPieces().contains(model.getBoard()[m.getDestX()][m.getDestY()])) {
                            if (model.getInteractionResult(m)) {
                                score += model.getBoard()[m.getDestX()][m.getDestY()].getValue() * 4 - p.getValue();
                            } else {
                                score -= p.getValue() * 4;
                            }
                        }
                        // if the opposing piece is not known, first, increase the value of the score as you are revealing a piece. then, calculate how worth it is to attack the piece
                        else {
                            score += 3 - p.getValue() * 1.5;
                            if (model.getBoard()[m.getDestX()][m.getDestY()].isRevealed()) {
                                score += 2 * p.getRank();
                            } else {
                                int b=0;
                                for(Piece enemyPiece:model.getFullBoard().getPlayerPieces()){
                                    if(enemyPiece.getType().equals("B")){
                                        b+=1;
                                    }
                                }
                                if (p.getType().equals("8")) {
                                    score += turnCount * 0.02 * b;
                                }
                                else{
                                    score += turnCount *(7-b)*0.02;
                                }
                            }
                        }
                    }
                    // if the move isn't an attacking move
                    // try to get closer to the known pieces with a piece that has a good matchup into most, while staying away from stronger pieces
                    else {
                        for (Piece enemyPiece : model.getFullBoard().getKnownPieces()) {
                            Piece p2 = new Piece(p.getType(), p.getRank(), p.getColor());
                            p2.setPosX(m.getDestX());
                            p2.setPosY(m.getDestY());
                            if (p.checkInteraction(enemyPiece)) {
                                if (p2.calcDistance(enemyPiece) < p.calcDistance(enemyPiece)) {
                                    score += 0.1;
                                    if(p.getType().equals("S")){
                                        score+=1;
                                    }
                                    if(p.getType().equals("8")){
                                        score+=0.2;
                                    }
                                }
                                else {
                                    score -= 0.1;
                                }
                            } else {
                                if (p2.calcDistance(enemyPiece) < p.calcDistance(enemyPiece)) {
                                    score -= 0.1;
                                } else {
                                    score += 0.1;
                                }
                            }
                        }
                    }


                    // add a random factor between 0.9 and 1 in the end
                    score *= (double) (randomFactor.nextInt(11) + 90) / 100;
                    if (score > maxScore) {
                        maxScore = score;
                        maxMove = m;
                    }
                }
                //repeat for the other 3 directions

                //forwards

                m = new Move(p.getPosX(), p.getPosY(), p.getPosX() + 1, p.getPosY());
                if (p.getPosX() != 10 && model.isMoveValid(m)) {
                    score = 0;
                    if (!p.isRevealed()) {
                        score -= 0.1;
                    }
                    // if the move is an attack
                    if (model.getBoard()[m.getDestX()][m.getDestY()] != null && model.getBoard()[m.getDestX()][m.getDestY()].getColor().equals("Blue")) {
                        // if the opposing piece is known, calculate how worth it it is to attack that piece
                        if (model.getFullBoard().getKnownPieces().contains(model.getBoard()[m.getDestX()][m.getDestY()])) {
                            if (model.getInteractionResult(m)) {
                                score += model.getBoard()[m.getDestX()][m.getDestY()].getValue() * 4 - p.getValue();
                            } else {
                                score -= p.getValue() * 4;
                            }
                        }
                        // if the opposing piece is not known, first, increase the value of the score as you are revealing a piece. then, calculate how worth it is to attack the piece
                        else {
                            score += 3 - p.getValue() * 1.5;
                            if (model.getBoard()[m.getDestX()][m.getDestY()].isRevealed()) {
                                score += 2 * p.getRank();
                            } else {
                                int b=0;
                                for(Piece enemyPiece:model.getFullBoard().getPlayerPieces()){
                                    if(enemyPiece.getType().equals("B")){
                                        b+=1;
                                    }
                                }
                                if (p.getType().equals("8")) {
                                    score += turnCount * 0.02 * b;
                                }
                                else{
                                    score += turnCount *(7-b)*0.02;
                                }
                            }
                        }
                    }
                    // if the move isn't an attacking move
                    // try to get closer to the known pieces with a piece that has a good matchup into most, while staying away from stronger pieces
                    else {
                        for (Piece enemyPiece : model.getFullBoard().getKnownPieces()) {
                            Piece p2 = new Piece(p.getType(), p.getRank(), p.getColor());
                            p2.setPosX(m.getDestX());
                            p2.setPosY(m.getDestY());
                            if (p.checkInteraction(enemyPiece)) {
                                if (p2.calcDistance(enemyPiece) < p.calcDistance(enemyPiece)) {
                                    score += 0.1;
                                    if(p.getType().equals("S")){
                                        score+=1;
                                    }
                                    if(p.getType().equals("8")){
                                        score+=0.2;
                                    }
                                }
                            }
                            else {
                                if (p2.calcDistance(enemyPiece) < p.calcDistance(enemyPiece)) {
                                    score -= 0.1;
                                } else {
                                    score += 0.1;
                                }
                            }
                        }
                    }


                    // add a random factor between 0.9 and 1 in the end
                    score *= (double) (randomFactor.nextInt(11) + 90) / 100;
                    if (score > maxScore) {
                        maxScore = score;
                        maxMove = m;
                    }
                }

                // left

                m = new Move(p.getPosX(), p.getPosY(), p.getPosX() , p.getPosY()-1);
                if (p.getPosY() != 0 && model.isMoveValid(m)) {
                    score = 0;
                    if (!p.isRevealed()) {
                        score -= 0.1;
                    }
                    // if the move is an attack
                    if (model.getBoard()[m.getDestX()][m.getDestY()] != null && model.getBoard()[m.getDestX()][m.getDestY()].getColor().equals("Blue")) {
                        // if the opposing piece is known, calculate how worth it it is to attack that piece
                        if (model.getFullBoard().getKnownPieces().contains(model.getBoard()[m.getDestX()][m.getDestY()])) {
                            if (model.getInteractionResult(m)) {
                                score += model.getBoard()[m.getDestX()][m.getDestY()].getValue() * 4 - p.getValue();
                            } else {
                                score -= p.getValue() * 4;
                            }
                        }
                        // if the opposing piece is not known, first, increase the value of the score as you are revealing a piece. then, calculate how worth it is to attack the piece
                        else {
                            score += 3 - p.getValue() * 1.5;
                            if (model.getBoard()[m.getDestX()][m.getDestY()].isRevealed()) {
                                score += 2 * p.getRank();
                            } else {
                                int b=0;
                                for(Piece enemyPiece:model.getFullBoard().getPlayerPieces()){
                                    if(enemyPiece.getType().equals("B")){
                                        b+=1;
                                    }
                                }
                                if (p.getType().equals("8")) {
                                    score += turnCount * 0.02 * b;
                                }
                                else{
                                    score += turnCount *(7-b)*0.02;
                                }
                            }
                        }
                    }
                    // if the move isn't an attacking move
                    // try to get closer to the known pieces with a piece that has a good matchup into most, while staying away from stronger pieces
                    else {
                        for (Piece enemyPiece : model.getFullBoard().getKnownPieces()) {
                            Piece p2 = new Piece(p.getType(), p.getRank(), p.getColor());
                            p2.setPosX(m.getDestX());
                            p2.setPosY(m.getDestY());
                            if (p.checkInteraction(enemyPiece)) {
                                if (p2.calcDistance(enemyPiece) < p.calcDistance(enemyPiece)) {
                                    score += 0.1;
                                    if(p.getType().equals("S")){
                                        score+=1;
                                    }
                                    if(p.getType().equals("8")){
                                        score+=0.2;
                                    }
                                }
                            }
                            else {
                                if (p2.calcDistance(enemyPiece) < p.calcDistance(enemyPiece)) {
                                    score -= 0.1;
                                } else {
                                    score += 0.1;
                                }
                            }
                        }
                    }


                    // add a random factor between 0.9 and 1 in the end
                    score *= (double) (randomFactor.nextInt(11) + 90) / 100;
                    if (score > maxScore) {
                        maxScore = score;
                        maxMove = m;
                    }
                }

                // right

                m = new Move(p.getPosX(), p.getPosY(), p.getPosX() , p.getPosY()+1);
                if (p.getPosY() != 10 && model.isMoveValid(m)) {
                    score = 0;
                    if (!p.isRevealed()) {
                        score -= 0.1;
                    }
                    // if the move is an attack
                    if (model.getBoard()[m.getDestX()][m.getDestY()] != null && model.getBoard()[m.getDestX()][m.getDestY()].getColor().equals("Blue")) {
                        // if the opposing piece is known, calculate how worth it it is to attack that piece
                        if (model.getFullBoard().getKnownPieces().contains(model.getBoard()[m.getDestX()][m.getDestY()])) {
                            if (model.getInteractionResult(m)) {
                                score += model.getBoard()[m.getDestX()][m.getDestY()].getValue() * 4 - p.getValue();
                            } else {
                                score -= p.getValue() * 4;
                            }
                        }
                        // if the opposing piece is not known, first, increase the value of the score as you are revealing a piece. then, calculate how worth it is to attack the piece
                        else {
                            score += 3 - p.getValue() * 1.5;
                            if (model.getBoard()[m.getDestX()][m.getDestY()].isRevealed()) {
                                score += 2 * p.getRank();
                            } else {
                                int b=0;
                                for(Piece enemyPiece:model.getFullBoard().getPlayerPieces()){
                                    if(enemyPiece.getType().equals("B")){
                                        b+=1;
                                    }
                                }
                                if (p.getType().equals("8")) {
                                    score += turnCount * 0.02 * b;
                                }
                                else{
                                    score += turnCount *(7-b)*0.02;
                                }
                            }
                        }
                    }
                    // if the move isn't an attacking move
                    // try to get closer to the known pieces with a piece that has a good matchup into most, while staying away from stronger pieces
                    else {
                        for (Piece enemyPiece : model.getFullBoard().getKnownPieces()) {
                            Piece p2 = new Piece(p.getType(), p.getRank(), p.getColor());
                            p2.setPosX(m.getDestX());
                            p2.setPosY(m.getDestY());
                            if (p.checkInteraction(enemyPiece)) {
                                if (p2.calcDistance(enemyPiece) < p.calcDistance(enemyPiece)) {
                                    score += 0.1;
                                    if(p.getType().equals("S")){
                                        score+=1;
                                    }
                                    if(p.getType().equals("8")){
                                        score+=0.2;
                                    }
                                }
                            }
                            else {
                                if (p2.calcDistance(enemyPiece) < p.calcDistance(enemyPiece)) {
                                    score -= 0.1;
                                } else {
                                    score += 0.1;
                                }
                            }
                        }
                    }

                    // add a random factor between 0.9 and 1 in the end
                    score *= (double) (randomFactor.nextInt(11) + 90) / 100;
                    if (score > maxScore) {
                        maxScore = score;
                        maxMove = m;
                    }
                }
            }
        }
        return maxMove;
    }


        /*
        // check if the piece has moved yet
        // if not, give a small score reduction to moving it to discourage movement
        if(p.hasMoved()){
           score-=p.getRank()*0.1;
        }
        // check if the move is initiating an attack
        // if so, calculate how good it is to go for the attack
        if(model.getBoard()[move.getDestX()][move.getDestY()]!=null){

        }

         */





    private Move firstThreeMoves(){
        ArrayList<Piece> firstRow = firstRowPieces();
        for(int i=0; i<9; i++){
            //look for scouts in the first row to gather information
            if (firstRow.get(i).getRank()==2){ // this means it is a scout
                Piece p = getClosetEnemyPiece(firstRow.get(i));
                // if p is not null
                // perform move
                // update the knowPieces array
                // return
                if(p!=null) {

                  //  knownPieces.add(p);
                    Move m = new Move(firstRow.get(i).getPosX(),firstRow.get(i).getPosY(),p.getPosX(),p.getPosY());
                    if(model.isMoveValid(m)){
                        return m;
                    }
                }

            }
        }

        return null;

    }

    private Piece getClosetEnemyPiece(Piece p) {
        for (int i = p.getPosX(); i < 10; i++) {
            if(model.getBoard()[i][p.getPosY()]!=null&&model.getBoard()[i][p.getPosY()].getColor().equals("Blue")) {
                return model.getBoard()[i][p.getPosY()];
            }
        }
        return null;
    }

    private Piece isImmediateThread()
    {
        Iterator enemyIt = model.getFullBoard().getPlayerPieces().iterator();
        for(Piece p:model.getFullBoard().getPlayerPieces()){
            if(flag.calcDistance(p)<=2){
                return p;
            }
        }
        return null;
    }

    private Move performImmediateDefence(Piece enemyPiece) {
        // 1 check if p is int known peices
        //      Defend with a piece that can beat him on offence

        if(model.getFullBoard().getKnownPieces().contains(enemyPiece))
        {
            // check in my pieces with distance 1 -> and attack
            for(Piece piece: model.getFullBoard().getComputerPieces())
            {
                if(piece.getRank() >= enemyPiece.getRank() && enemyPiece.calcDistance(piece) ==1)
                {
                    // move
                    // return
                    Move m=new Move(piece.getPosX(),piece.getPosY(), enemyPiece.getPosX(), enemyPiece.getPosY());
                    if(model.isMoveValid(m)){
                        return m;
                    }
                }
            }

        }

        // 2 if p is not in known pieces:
        //      check in my pieces if there is a a piece distance 1 to enemy
        //         if there is -> find highest rank and attack


        Piece highestRank = null;
        int bestRank = Integer.MIN_VALUE;

        for(Piece piece: model.getFullBoard().getComputerPieces())
        {
            if((piece.checkInteraction(enemyPiece)||piece.getRank()==enemyPiece.getRank())&& enemyPiece.calcDistance(piece)==1)
            {
                if(piece.getRank()>bestRank){
                    highestRank = piece;
                    bestRank= highestRank.getRank();
                }
            }
        }

        if(highestRank != null )
        {
            // make move
            // return

            return new Move(highestRank.getPosX(), highestRank.getPosY(), enemyPiece.getPosX(), enemyPiece.getPosY());
        }


        //     if there is no such piece -. find highest rank  distance 2 and move closer...
        for(Piece piece: model.getFullBoard().getComputerPieces())
        {
            if(piece.getRank() > bestRank && enemyPiece.calcDistance(piece)==2)
            {
                highestRank = piece;
                bestRank= highestRank.getRank();


            }
        }
        //     we can move right up left down
        //     if no?  general move -> return
        if(highestRank!=null){return model.getCloserToOtherPiece(highestRank,enemyPiece);}
        //     if no?  general move -> return
        //     CHANGE TO GENERAL MOVE FUNCTION WHEN THAT FUNCTION GETS WRITTEN
        return null;





    }

    int turnCount=0;
    private Move generateMove() {
        List<Move> possibleMoves = new ArrayList<>();
        Piece[][] board = model.getBoard();

        if(turnCount<=2) {
            Move m = firstThreeMoves();
            turnCount++;

            return m;
        }



        Piece p = isImmediateThread();
        if(p!=null)
        {
            Move m = performImmediateDefence(p);
            // this means that there is an immediate thread
            // if there is a move to stop it -> return the move
            if(m!=null) {
                turnCount++;
                return m;
            }
        }


        if(model.getFullBoard().getKnownPieces().size() < 6&& turnCount<25)
        {
            turnCount++;
            return openingStage();
        }

        turnCount++;
        return generalMove();




        //



        // If no valid move is found, return null
    }
    //


}