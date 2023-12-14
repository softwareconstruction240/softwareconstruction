package chess;

import chess.*;
import chess.pieces.*;
import com.google.gson.*;

public class ChessSerialization {

    public static Gson createGsonSerializer() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        // This line should only be needed if your board class is using a Map to store chess pieces instead of a 2D array.
        gsonBuilder.enableComplexMapKeySerialization();

        gsonBuilder.registerTypeAdapter(ChessGame.class,
                (JsonDeserializer<ChessGame>) (el, type, ctx) -> ctx.deserialize(el, ChessGameImpl.class));

        gsonBuilder.registerTypeAdapter(ChessBoard.class,
                (JsonDeserializer<ChessBoard>) (el, type, ctx) -> ctx.deserialize(el, ChessBoardImpl.class));

        gsonBuilder.registerTypeAdapter(ChessPiece.class,
                (JsonDeserializer<ChessPiece>) (el, type, ctx) -> ctx.deserialize(el, ChessPieceImpl.class));

        gsonBuilder.registerTypeAdapter(ChessMove.class,
                (JsonDeserializer<ChessMove>) (el, type, ctx) -> ctx.deserialize(el, ChessMoveImpl.class));

        gsonBuilder.registerTypeAdapter(ChessPosition.class,
                (JsonDeserializer<ChessPosition>) (el, type, ctx) -> ctx.deserialize(el, ChessPositionImpl.class));

        gsonBuilder.registerTypeAdapter(ChessPieceImpl.class,
                (JsonDeserializer<ChessPieceImpl>) (el, type, ctx) -> {
                    ChessPieceImpl chessPiece = null;
                    if (el.isJsonObject()) {
                        String pieceType = el.getAsJsonObject().get("type").getAsString();
                        switch (ChessPiece.PieceType.valueOf(pieceType)) {
                            case PAWN -> chessPiece = ctx.deserialize(el, Pawn.class);
                            case ROOK -> chessPiece = ctx.deserialize(el, Rook.class);
                            case KNIGHT -> chessPiece = ctx.deserialize(el, Knight.class);
                            case BISHOP -> chessPiece = ctx.deserialize(el, Bishop.class);
                            case QUEEN -> chessPiece = ctx.deserialize(el, Queen.class);
                            case KING -> chessPiece = ctx.deserialize(el, King.class);
                        }
                    }
                    return chessPiece;
                });

        return gsonBuilder.create();
    }
}
