declare module gameStateMessage {

    export interface HasName {
    }

    export interface HasPlayerData {
    }

    export interface HasHP {
    }

    export interface RootObject {
        HasName: HasName;
        HasPlayerData: HasPlayerData;
        HasHP: HasHP;
    }
}