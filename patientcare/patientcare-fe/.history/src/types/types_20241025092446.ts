export interface Fragment {
  fragmentId: number
  fragmentText: string
  lexiconName: string
  authorLastName: string
  authorFirstName: string
  processingState: ProcessingState
  metaData: { data: any[] }
  typeInfo: TypeInfo | null
  eventId: number | undefined
  articleId: number
}

export enum ProcessingState {
  DONE = 'DONE'
}

export interface LexiconEntry {
  articleId: number
  lexiconName: string
  lexiconFullName: string
  lexiconStartYear: number
  lexiconEndYear: number | null
  author: string
  fragments: Fragment[]
}

export interface Lexicon {
  id: number
  lexiconName: string
  lexiconFullName: string | null
  startYear: number | null
  endYear: number | null
  articles: LexiconEntry[]
}

export interface Author {
  authorName: string
  lexicons: string[]
  lexiconEntries: LexiconEntry[] | null
}

export interface TypeInfo {
  typeName: string
  activeColor: string
  inActiveColor: string
  borderColor: string
}

export interface MetaData {
  event: {
    id: Number
  }
  attribution: {
    entity: {
      id: Number
      displayLabel: string
      date: string
    }
  }
  attrname: string
  value: string
}

export interface StoreDataObject {
  comparisonData: LexiconEntry[]
  comparisonMode: string | null
  author: string | null
  lexiconName: string | null
  selectedFragment: Fragment | null
}

export interface EventCounter {
  eventId: number
  counter: number | 0
}

export interface MatchingEvent {
  operation: 'highlight' | 'showMeta' | ''
  initiatorId: number | null
  eventId: number | null
}

export type TranslationTable = {
  [grammaticalCase: string]: {
    [key: string]: string
  }
}

export interface EventArticleIndex {
  articleId: number
  counters: EventCounter[]
}
