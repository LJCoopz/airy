module.exports = {
  docs: [
    {
      '🚀 Getting Started': [
        'getting-started/introduction',
        {
          Installation: [
            'getting-started/installation/introduction',
            'getting-started/installation/vagrant',
            'getting-started/installation/production',
            'getting-started/installation/configuration',
          ],
        },
        {
          'Command Line Interface': ['cli/introduction', 'cli/installation', 'cli/reference'],
        },
        'getting-started/quickstart',
        'getting-started/troubleshooting',
        'getting-started/glossary',
      ],
    },
    {
      '💬 Sources': [
        'sources/introduction',
        'sources/chat-plugin',
        'sources/facebook',
        'sources/google',
        'sources/sms-twilio',
        'sources/whatsapp-twilio',
      ],
    },
    {
      '🔌 API': [
        'api/introduction',
        'api/authentication',
        {
          'HTTP Endpoints': [
            'api/endpoints/introduction',
            'api/endpoints/channels',
            'api/endpoints/conversations',
            'api/endpoints/messages',
            'api/endpoints/metadata',
            'api/endpoints/tags',
            'api/endpoints/users',
          ],
        },
        'api/websocket',
        'api/webhook',
      ],
    },
    {
      '✨ Apps': [
        {
          UI: ['apps/ui/introduction', 'apps/ui/quickstart', 'apps/ui/inbox', 'apps/ui/tags', 'apps/ui/components'],
        },
      ],
    },
    {
      '🛠️ Integrations': [
        {
          'Conversational AI /NLP': ['integrations/rasa'],
        },
      ],
    },
    {
      '⚙️ Concepts': [
        'concepts/architecture',
        'concepts/design-principles',
        'concepts/release-process',
        'concepts/kafka',
      ],
    },
    {
      '📚 Guides': ['guides/contributing'],
    },
  ],
};
