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


    private void openingStage(){
       double score;
       double maxScore=-999;
       Move m;
       Move maxMove;
       // go over every piece
       for(Piece p:model.getFullBoard().getComputerPieces()){
           // go over every movable piece
           if (model.isMovablePiece(p.getPosX(),p.getPosY())){
               m=new Move(p.getPosX(),p.getPosY(),p.getPosX()-1,p.getPosY());
               if(p.getPosX()!=0&& model.isMoveValid(m)){
                   score=0;
                   if(!p.isRevealed()){
                       score-=p.getValue()*0.2;
                   }
                   // if the move is an attack
                   if(model.getBoard()[m.getDestX()][m.getDestY()]!=null&& model.getBoard()[m.getDestX()][m.getDestY()].getColor().equals("Blue")){
                       // if the opposing piece is known, calculate how worth it it is to attack that piece
                       if(model.getFullBoard().getKnownPieces().contains(model.getBoard()[m.getDestX()][m.getDestY()])){
                           if(model.getInteractionResult(m)){
                               score+=model.getBoard()[m.getDestX()][m.getDestY()].getValue()*3-p.getValue();
                           }
                           else{
                               score-=p.getValue()*3;
                           }
                       }
                       // if the opposing piece is not known, first, increase the value of the score as you are revealing a piece. then, calculate how worth it is to attack the piece
                       else{
                           score+=10-p.getValue()*1.5;
                           for(Piece enemyPiece:model.getFullBoard().getPlayerPieces()){
                               if(p.checkInteraction(enemyPiece)){
                                   score+=0.1*((double) 40 /model.getFullBoard().getPlayerPieces().size());
                               }
                               else{
                                   score-=p.getValue()*0.1*((double) 40 /model.getFullBoard().getPlayerPieces().size());
                               }
                           }
                       }
                       //if the move isn't an attacking move

                       // add a random factor between 0.9 and 1 in the end
                       score*=(double)(randomFactor.nextInt(11)+90)/100;
                       if(score>maxScore){
                           maxScore=score;
                           maxMove=m;
                       }
                   }
               }
               //repeat for the other 3 directions

               //forwards

               m=new Move(p.getPosX(),p.getPosY(),p.getPosX()+1,p.getPosY());
               if(p.getPosX()!=10&& model.isMoveValid(m)){
                   score=0;
                   if(!p.isRevealed()){
                       score-=p.getValue()*0.2;
                   }
                   // if the move is an attack
                   if(model.getBoard()[m.getDestX()][m.getDestY()]!=null&& model.getBoard()[m.getDestX()][m.getDestY()].getColor().equals("Blue")){
                       // if the opposing piece is known, calculate how worth it it is to attack that piece
                       if(model.getFullBoard().getKnownPieces().contains(model.getBoard()[m.getDestX()][m.getDestY()])){
                           if(model.getInteractionResult(m)){
                               score+=model.getBoard()[m.getDestX()][m.getDestY()].getValue()*3-p.getValue();
                           }
                           else{
                               score-=p.getValue()*3;
                           }
                       }
                       // if the opposing piece is not known, first, increase the value of the score as you are revealing a piece. then, calculate how worth it is to attack the piece
                       else{
                           score+=10-p.getValue()*1.5;
                           for(Piece enemyPiece:model.getFullBoard().getPlayerPieces()){
                               if(p.checkInteraction(enemyPiece)){
                                   score+=0.1*((double) 40 /model.getFullBoard().getPlayerPieces().size());
                               }
                               else{
                                   score-=p.getValue()*0.1*((double) 40 /model.getFullBoard().getPlayerPieces().size());
                               }
                           }
                       }
                       //if the move isn't an attacking move

                       // add a random factor between 0.9 and 1 in the end
                       score*=(double)(randomFactor.nextInt(11)+90)/100;
                       if(score>maxScore){
                           maxScore=score;
                           maxMove=m;
                       }
                   }

                   // left

                   m=new Move(p.getPosX(),p.getPosY(),p.getPosX(),p.getPosY()-1);
                   if(p.getPosX()!=10&& model.isMoveValid(m)) {
                       score = 0;
                       if (!p.isRevealed()) {
                           score -= p.getValue() * 0.2;
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
                           //if the move isn't an attacking move

                           // add a random factor between 0.9 and 1 in the end
                           score *= (double) (randomFactor.nextInt(11) + 90) / 100;
                           if (score > maxScore) {
                               maxScore = score;
                               maxMove = m;
                           }
                       }
                   }

                   // right

                   m=new Move(p.getPosX(),p.getPosY(),p.getPosX(),p.getPosY()+1);
                   if(p.getPosX()!=10&& model.isMoveValid(m)) {
                       score = 0;
                       if (!p.isRevealed()) {
                           score -= p.getValue() * 0.2;
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
                           //if the move isn't an attacking move

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




    }
    private Move firstTwoMoves(){
        ArrayList<Piece> firstRow = firstRowPieces();
        for(int i=0; i<10; i++){
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
                    return m;

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
        //      chekc in my pieces if there is a a piece distance 1 to enemy
        //         if there is -> find hisghest rank and attack


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
            Move m = firstTwoMoves();
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


        if(model.getFullBoard().getKnownPieces().size() < 10)
        {
            turnCount++;
            openingStage();
        }





        //

        // Generate all possible moves
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Piece piece = board[x][y];
                //Generate all non scout moves
                if (piece != null && piece.getColor().equals("Red") && piece.getRank()!=2 && piece.getRank()!=-1) {
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            if (dx == 0 && dy == 0) continue; // Skip the current cell
                            int newX = x + dx;
                            int newY = y + dy;
                            if (newX >= 0 && newX < 10 && newY >= 0 && newY < 10) {
                                Move move = new Move(x, y, newX, newY);
                                if (model.isMoveValid(move)) {
                                    possibleMoves.add(move);
                                }
                            }
                        }
                    }
                }
                // Generate all scout moves
                else{
                    if(piece != null && piece.getColor().equals("Red") && piece.getRank()==2){
                        for (int dx = -9; dx <= 9; dx++) {
                            for (int dy = -9; dy <= 9; dy++) {
                                if (dx == 0 && dy == 0) continue; // Skip the current cell
                                int newX = x + dx;
                                int newY = y + dy;
                                if (newX >= 0 && newX < 10 && newY >= 0 && newY < 10) {
                                    Move move = new Move(x, y, newX, newY);
                                    if (model.isMoveValid(move)) {
                                        possibleMoves.add(move);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Prioritize moves that capture an opponent's piece
        for (Move move : possibleMoves) {
            Piece destPiece = board[move.getDestX()][move.getDestY()];
            if (destPiece != null && destPiece.getColor().equals("Blue") && model.getInteractionResult(move)) {
                return move;
            }
        }

        // If no capturing move is found, fall back to a random move
        if (!possibleMoves.isEmpty()) {
            return possibleMoves.get(randomFactor.nextInt(possibleMoves.size()));
        }

        // If no valid move is found, return null
        return null;
    }
    //


}